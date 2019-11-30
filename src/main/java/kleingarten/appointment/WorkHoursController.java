package kleingarten.appointment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WorkHoursController {

	private AppointmentManager appointmentManager;
	private WorkHoursManager workHoursManager;

	public WorkHoursController(AppointmentManager appointmentManager, WorkHoursManager workHoursManager){
		this.appointmentManager = appointmentManager;
		this.workHoursManager = workHoursManager;
	}

	@GetMapping("/addAppointment")
	public String appointmentList(Model model){
		model.addAttribute("ListofAppointments", appointmentManager.getAll());
		return "appointmentList";
	}

	@PostMapping("/addAppointment")
	public String addAppointment(AddAppointmentForm form){
		workHoursManager.addAppointment(form);
		return "redirect:/myPlot";
	}
}
