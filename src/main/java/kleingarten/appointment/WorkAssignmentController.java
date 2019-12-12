package kleingarten.appointment;

import kleingarten.plot.Plot;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class WorkAssignmentController {

	private WorkAssignmentManager workAssignmentManager;
	private WorkAssignmentRepository workAssignmentRepository;

	public WorkAssignmentController(WorkAssignmentManager workAssignmentManager, WorkAssignmentRepository workAssignmentRepository) {
		this.workAssignmentManager = workAssignmentManager;
		this.workAssignmentRepository = workAssignmentRepository;
	}


	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@GetMapping("/createAssignment")
	public String appointmentList(Model model, CreateWorkAssignmentForm form) {
		model.addAttribute("dateNow", LocalDate.now());
		model.addAttribute("timeNow", LocalTime.now());
		model.addAttribute("form", form);
		return "workAssignment/createAssignment";
	}


	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@PostMapping("/createAssignment")
	public String addAppointment(@Valid CreateWorkAssignmentForm form) {
		if (!workAssignmentManager.containsListTheDate(form.getDateTime())) {
			workAssignmentManager.createAssignment(form);
			System.out.println("true");
			return "redirect:/createAssignment";
		}
		System.out.println("false");
		return "redirect:/createAssignment";
	}


	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@GetMapping("/listOfAssignments")
	String listOfAssignments(Model model) {
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		return "workAssignment/listOfAssignments";
	}


	@GetMapping("/workAssignmentModify")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String modifyWorkAssignment(@RequestParam("id") Long id, Model model){
		model.addAttribute("workAssignment", workAssignmentManager.findByID(id));
		return "workAssignment/workAssignmentModify";
	}



	@PostMapping("/modifiedWorkAssignment")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String modifiedWorkAssignment(@RequestParam("id")		  Long id,
						   @RequestParam("title") String title,
						   @RequestParam("description")  String description){

		workAssignmentManager.findByID(id).setTitle(title);
		workAssignmentManager.findByID(id).setDescription(description);
		workAssignmentRepository.save(workAssignmentManager.findByID(id));

		return "redirect:/listOfAssignments";
	}

	@GetMapping("/listForPlotAssignments/{plotID}")
	String getForPlotWorkAssignments(@PathVariable ProductIdentifier plotID, Model model){
		model.addAttribute("ListWorkAssignmentsForPlot", workAssignmentManager.getForPlotWorkAssignments(plotID));
		model.addAttribute("plotID", plotID);
		return "workAssignment/listForPlotAssignments";
	}

	@GetMapping("/addWorkAssignment/{plotID}")
	public String workAssignmentToPlot(@PathVariable ProductIdentifier plotID,  Model model) {
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		model.addAttribute("plotID", plotID);
		return "workAssignment/addWorkAssignment";
	}


	@PostMapping("/addWorkAssignment/{plotID}")
	public String AddWorkAssignmentToPlot(@PathVariable ProductIdentifier plotID, @RequestParam("WorkAssignmentID") Long workAssignmentID){
		workAssignmentManager.addPlotToWorkAssignment(plotID, workAssignmentID);
		return "redirect:/myPlot";
	}


	@GetMapping("/addWorkHours")
	String addWorkHours(Model model){
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		return "workAssignment/addWorkHours";
	}

	@PostMapping("/changeWorkHours")
	String setWorkHours(@RequestParam("id") Long id,
						@RequestParam("workHours") String workHours){
		int workHoursInt = Integer.parseInt(workHours);
		workAssignmentManager.setWorkHours(workHoursInt , id);
		return "redirect:/addWorkHours";
	}
}
