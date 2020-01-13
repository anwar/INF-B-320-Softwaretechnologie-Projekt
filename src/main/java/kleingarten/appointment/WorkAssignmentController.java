package kleingarten.appointment;


import kleingarten.tenant.Tenant;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Controller
public class WorkAssignmentController {

	private WorkAssignmentManager workAssignmentManager;
	private WorkAssignmentRepository workAssignmentRepository;
	private WorkAssignmentTimer workAssignmentTimer;

	/**
	 * Constructor of class {@link WorkAssignmentController}
	 * @param workAssignmentManager manager class {@link WorkAssignmentManager}
	 * @param workAssignmentRepository repository of WorkAssignments as {@link WorkAssignmentRepository}
	 * @param workAssignmentTimer workAssignmentTimer as Counter {@link WorkAssignmentTimer}
	 */
	public WorkAssignmentController(WorkAssignmentManager workAssignmentManager,
									WorkAssignmentRepository workAssignmentRepository, WorkAssignmentTimer workAssignmentTimer) {
		this.workAssignmentManager = workAssignmentManager;
		this.workAssignmentRepository = workAssignmentRepository;
		this.workAssignmentTimer = workAssignmentTimer;
	}


	/**
	 * create a {@link WorkAssignment}
	 * @param model view to show the date, the time of now. Show maximal Date.
	 * @param form is the form for create a {@link WorkAssignment}
	 * @return put the data from form {@link CreateWorkAssignmentForm} to the html template
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@GetMapping("/createAssignment")
	public String appointmentList(Model model, CreateWorkAssignmentForm form) {
		int year = LocalDate.now().getYear() + 10;
		int month = 1;
		int day = 1;
		LocalDate localDate = LocalDate.of(year, month, day);
		System.out.println(localDate);
		model.addAttribute("dateNow", LocalDate.now());
		model.addAttribute("timeNow", LocalTime.now());
		model.addAttribute("dateMax", localDate);
		model.addAttribute("form", form);
		return "workAssignment/createAssignment";
	}


	/**
	 * create a {@link WorkAssignment}
	 * @param form {@link CreateWorkAssignmentForm} is the form for create a {@link WorkAssignment}
	 * @return link to "/createWorkAssignment" and add  {@link WorkAssignment} to {@link java.util.List} in
	 * {@link WorkAssignmentManager}
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@PostMapping("/createAssignment")
	public String addAppointment(@Valid CreateWorkAssignmentForm form) {
		if (!workAssignmentManager.containsListTheDate(form.getDateTime())) {
			workAssignmentManager.createAssignment(form);
			return "redirect:/createAssignment";
		}
		return "redirect:/createAssignment";
	}


	/**
	 * Shows an overview of {@link WorkAssignment} as a list
	 * @param model view to show list of {@link WorkAssignment}s as {@link Model}
	 * @param userAccount is proof for the role of user
	 * @return html to show list of {@link WorkAssignment}s
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	@GetMapping("/listOfAssignments")
	public String listOfAssignments(Model model, @LoggedIn Optional<UserAccount> userAccount) {
		if (userAccount.isEmpty()) {
			return "redirect:/login";
		}

		System.out.println(workAssignmentTimer.getPeriod(userAccount.get()));
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		return "workAssignment/listOfAssignments";
	}


	/**
	 * View to edit a {@link WorkAssignment}s details
	 * @param id is for identifying which work assignment should be changed
	 * @param model shows the number of {@link kleingarten.plot.Plot}s in the {@link WorkAssignment}
	 * @return html for editing a {@link WorkAssignment}
	 */
	@GetMapping("/workAssignmentModify")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	public String modifyWorkAssignment(@RequestParam("id") Long id, Model model) {
		model.addAttribute("workAssignment", workAssignmentManager.findByID(id));
		model.addAttribute("plotListForWorkAssignment", workAssignmentManager.getPlotsInWorkAssignment(id));
		return "workAssignment/workAssignmentModify";
	}


	/**
	 * Getter for the changed details of a {@link WorkAssignment} to save in the {@link WorkAssignment}
	 * @param id identifier of {@link WorkAssignment} as {@link Long}
	 * @param title input title as {@link String}
	 * @param description input description as {@link String}
	 * @param workHours input workHours as {@link Integer}
	 * @return html of the {@link WorkAssignment}s html with saved new details of {@link WorkAssignment}
	 */
	@PostMapping("/modifiedWorkAssignment")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	public String modifiedWorkAssignment(@RequestParam("id") Long id,
								  @RequestParam("title") String title,
								  @RequestParam("description") String description,
								  @RequestParam("workHours") int workHours) {

		workAssignmentManager.findByID(id).setTitle(title);
		workAssignmentManager.findByID(id).setDescription(description);
		workAssignmentManager.findByID(id).setWorkHours(workHours);
		workAssignmentRepository.save(workAssignmentManager.findByID(id));

		return "redirect:/listOfAssignments";
	}

	/** Remove a {@link kleingarten.plot.Plot} from a {@link WorkAssignment}
	 * @param plotID to identifier the {@link kleingarten.plot.Plot}
	 * @param assignmentID to identifier the {@link WorkAssignment}
	 * @return update the new html template with new numbers of {@link kleingarten.plot.Plot}'s
	 */
	@PostMapping("/removePlotOfWorkAssignment")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	public String removePlot(@RequestParam("plotID") ProductIdentifier plotID,
					  @RequestParam("assignmentID") Long assignmentID) {
		workAssignmentManager.removePlotOutWorkAssignment(plotID, assignmentID);
		System.out.println("debug after function in Controller");
		return "redirect:/listOfAssignments";
	}

	/**
	 * gives the {@link WorkAssignment}'s from a specific {@link kleingarten.plot.Plot}
	 * @param plotID to identifier the {@link kleingarten.plot.Plot}
	 * @param model shows the {@link java.util.List} of all {@link WorkAssignment}'s
	 * and shows all specific {@link WorkAssignment}'s of one {@link kleingarten.plot.Plot}
	 * @param userAccount is proof for the role of user
	 * @return a {@link java.util.List} of all {@link WorkAssignment}'s of one {@link kleingarten.plot.Plot}
	 */
	@GetMapping("/listForPlotAssignments/{plotID}")
	public String getForPlotWorkAssignments(@PathVariable ProductIdentifier plotID, Model model, @LoggedIn Optional<UserAccount> userAccount) {
		if (userAccount.isEmpty()) {
			return "redirect:/login";
		}

		System.out.println(workAssignmentTimer.getPeriod(userAccount.get()));
		model.addAttribute("ListWorkAssignmentsForPlot", workAssignmentManager.getForPlotWorkAssignments(plotID));
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		model.addAttribute("plotID", plotID);
		return "workAssignment/listForPlotAssignments";
	}

	/**
	 * add a {@link WorkAssignment} to a {@link kleingarten.plot.Plot}
	 * @param plotID to identifier the {@link kleingarten.plot.Plot}
	 * @param id to identifier the {@link WorkAssignment}
	 * @return shows the new {@link WorkAssignment}'s, they be added, in "/myPlot"
	 */
	@PostMapping("/setWorkAssignment")
	public String AddWorkAssignmentToPlot(@RequestParam("plotID") ProductIdentifier plotID,
										  @RequestParam("assignmentID") Long id) {
		workAssignmentManager.addPlotToWorkAssignment(plotID, id);
		return "redirect:/myPlot";
	}

	/**
	 * Getter for all {@link WorkAssignment}'s
	 * @param model gives the {@link java.util.List} of all {@link WorkAssignment}'s
	 * @return the html template with all {@link WorkAssignment}'s
	 */
	@GetMapping("/addWorkHours")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	public String addWorkHours(Model model) {
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		return "workAssignment/addWorkHours";
	}

	/**
	 * Setter for WorkHours by all {@link WorkAssignment}'s
	 * @param id to identifier the {@link WorkAssignment}
	 * @param workHours gives the old value and set new value of work hour
	 * @return show the updated html site with the new values of work hours
	 */
	@PostMapping("/changeWorkHours")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	public String setWorkHours(@RequestParam("id") Long id,
						@RequestParam("workHours") String workHours) {
		int workHoursInt = Integer.parseInt(workHours);
		workAssignmentManager.setWorkHours(workHoursInt, id);
		return "redirect:/addWorkHours";
	}

	/**
	 * Remove a {@link WorkAssignment}
	 * @param id to identifier the {@link WorkAssignment}
	 * @return shows the updated html site without the deleted {@link WorkAssignment}
	 */
	@PostMapping("/removeWorkAssignment")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	public String removeWorkAssignment(@RequestParam("id") Long id) {
		System.out.println(id);
		workAssignmentManager.removeWorkAssignment(id);
		return "redirect:/listOfAssignments";
	}
}
