package kleingarten.appointment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class WorkAssignmentController {

	private WorkAssignmentManager workAssignmentManager;

	public WorkAssignmentController(WorkAssignmentManager workAssignmentManager){
		this.workAssignmentManager = workAssignmentManager;
	}

	@GetMapping("/createAssignment")
	public String appointmentList(Model model,CreateWorkAssignmentForm form){
		model.addAttribute("dateNow", LocalDate.now());
		model.addAttribute("timeNow", LocalTime.now());
		model.addAttribute("form", form);
		return "/workAssignment/createAssignment";
	}

	@PostMapping("/createAssignment")
	public String addAppointment(@Valid CreateWorkAssignmentForm form, Error error) {
		String date = form.getDate() + " " + form.getTime();
		LocalDateTime localDateTime = LocalDateTime.parse(date);
		if (workAssignmentManager.getAll().contains(localDateTime)){
			error.getMessage();
		}else{
			workAssignmentManager.createAssignment(form);
		}
		return "redirect:/workAssignment/createAssignment";
	}

	@GetMapping("/listOfAssignments")
	String listOfAppointments(Model model){
		model.addAttribute("ListOfAssignments", workAssignmentManager.getAll());
		return "/workAssignment/listOfAssignments";
	}




	/*
	@GetMapping("/addAppointment/{plotID}")
	public String appointmentList(Model model, @PathVariable SalespointIdentifier plotID){
		model.addAttribute("ListofAppointments", workAssignmentManager.getAll());
		//model.addAttribute("ListOfTimes", appointmentManager.getAll().get().m
		//alle einsätze auflisten
		return "addAppointment";
	}


	@PostMapping("/addAppointment{plotID}")
	public String addAppointment(CreateWorkAssignmentForm form, @PathVariable SalespointIdentifier plotID){
		workAssignmentManager.addAppointment();
		return "redirect:/myPlot";
	}
	*/
}
