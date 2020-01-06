package kleingarten.appointment;

import org.salespointframework.catalog.ProductIdentifier;
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

@Controller
public class WorkAssignmentController {

	private WorkAssignmentManager workAssignmentManager;
	private WorkAssignmentRepository workAssignmentRepository;

	public WorkAssignmentController(WorkAssignmentManager workAssignmentManager,
									WorkAssignmentRepository workAssignmentRepository) {
		this.workAssignmentManager = workAssignmentManager;
		this.workAssignmentRepository = workAssignmentRepository;
	}


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


	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@PostMapping("/createAssignment")
	public String addAppointment(@Valid CreateWorkAssignmentForm form) {
		if (!workAssignmentManager.containsListTheDate(form.getDateTime())) {
			workAssignmentManager.createAssignment(form);
			return "redirect:/createAssignment";
		}
		return "redirect:/createAssignment";
	}


	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	@GetMapping("/listOfAssignments")
	String listOfAssignments(Model model) {
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		return "workAssignment/listOfAssignments";
	}


	@GetMapping("/workAssignmentModify")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	String modifyWorkAssignment(@RequestParam("id") Long id, Model model) {
		model.addAttribute("workAssignment", workAssignmentManager.findByID(id));
		model.addAttribute("plotListForWorkAssignment", workAssignmentManager.getPlotsInWorkAssignment(id));
		return "workAssignment/workAssignmentModify";
	}


	@PostMapping("/modifiedWorkAssignment")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	String modifiedWorkAssignment(@RequestParam("id") Long id,
								  @RequestParam("title") String title,
								  @RequestParam("description") String description,
								  @RequestParam("workHours") int workHours) {

		workAssignmentManager.findByID(id).setTitle(title);
		workAssignmentManager.findByID(id).setDescription(description);
		workAssignmentManager.findByID(id).setWorkHours(workHours);
		workAssignmentRepository.save(workAssignmentManager.findByID(id));

		return "redirect:/listOfAssignments";
	}

	@PostMapping("/removePlotOfWorkAssignment")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	String removePlot(@RequestParam("plotID") ProductIdentifier plotID,
					  @RequestParam("assignmentID") Long assignmentID) {
		workAssignmentManager.removePlotOutWorkAssignment(plotID, assignmentID);
		System.out.println("debug after function in Controller");
		return "redirect:/listOfAssignments";
	}

	@GetMapping("/listForPlotAssignments/{plotID}")
	String getForPlotWorkAssignments(@PathVariable ProductIdentifier plotID, Model model) {
		model.addAttribute("ListWorkAssignmentsForPlot", workAssignmentManager.getForPlotWorkAssignments(plotID));
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		model.addAttribute("plotID", plotID);
		return "workAssignment/listForPlotAssignments";
	}

	@PostMapping("/setWorkAssignment")
	public String AddWorkAssignmentToPlot(@RequestParam("plotID") ProductIdentifier plotID,
										  @RequestParam("assignmentID") Long id) {
		workAssignmentManager.addPlotToWorkAssignment(plotID, id);
		return "redirect:/myPlot";
	}

	@GetMapping("/addWorkHours")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	String addWorkHours(Model model) {
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		return "workAssignment/addWorkHours";
	}

	@PostMapping("/changeWorkHours")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	String setWorkHours(@RequestParam("id") Long id,
						@RequestParam("workHours") String workHours) {
		int workHoursInt = Integer.parseInt(workHours);
		workAssignmentManager.setWorkHours(workHoursInt, id);
		return "redirect:/addWorkHours";
	}

	@PostMapping("/removeWorkAssignment")
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	String removeWorkAssignment(@RequestParam("id") Long id) {
		System.out.println(id);
		workAssignmentManager.removeWorkAssignment(id);
		return "redirect:/listOfAssignments";
	}
}
