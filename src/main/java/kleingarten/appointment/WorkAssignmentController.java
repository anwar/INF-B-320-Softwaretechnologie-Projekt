package kleingarten.appointment;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.SalespointIdentifier;
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

	public WorkAssignmentController(WorkAssignmentManager workAssignmentManager) {
		this.workAssignmentManager = workAssignmentManager;
	}

	@GetMapping("/createAssignment")
	public String appointmentList(Model model, CreateWorkAssignmentForm form) {
		model.addAttribute("dateNow", LocalDate.now());
		model.addAttribute("timeNow", LocalTime.now());
		model.addAttribute("form", form);
		return "workAssignment/createAssignment";
	}

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

	@GetMapping("/listOfAssignments")
	String listOfAppointments(Model model) {
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		return "workAssignment/listOfAssignments";
	}


	@GetMapping("/addWorkAssignment/{plotID}")
	public String appointmentList(@PathVariable ProductIdentifier plotID,  Model model) {
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		return "workAssignment/addWorkAssignment";
	}


	@PostMapping("/addWorkAssignment/{plotID}/{workAssigmentID}")
	public String addAppointment(Model model, @PathVariable ProductIdentifier plotID, @PathVariable long workAssigmentID) {
		workAssignmentManager.addWorkAssignment(plotID, workAssigmentID);
		return "redirect:/myPlot";
	}
}
