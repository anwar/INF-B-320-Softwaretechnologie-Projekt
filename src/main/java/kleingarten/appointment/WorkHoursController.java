package kleingarten.appointment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WorkHoursController {

	private AppointmentManager appointmentManager;
	private WorkHoursManager workHoursManager;

	public WorkHoursController(AppointmentManager appointmentManager, WorkHoursManager workHoursManager){
		this.appointmentManager = appointmentManager;
		this.workHoursManager = workHoursManager;
	}

	@GetMapping("/addAppointment")
	public String addAppointment(){
		return "addAppointment";
	}
}
