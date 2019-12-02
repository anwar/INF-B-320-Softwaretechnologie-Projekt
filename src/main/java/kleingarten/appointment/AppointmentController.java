package kleingarten.appointment;

import com.mysema.commons.lang.Assert;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class AppointmentController {

	private AppointmentManager appointmentManager;

	public AppointmentController(AppointmentManager appointmentManager){
		Assert.notNull(appointmentManager,"AppointmentManager must not be null");
		this.appointmentManager = appointmentManager;
	}

	@GetMapping("/createAppointment")
	String Appointments (Model model, CreateAppointmentForm form){
		model.addAttribute("dateNow", LocalDate.now());
		model.addAttribute("timeNow", LocalTime.now());
		model.addAttribute("form", form);
		return "createAppointment";
	}

	@PostMapping("/createAppointment")
	String addAppointment(@Valid CreateAppointmentForm form, Error error){
		LocalDate localDate = LocalDate.parse(form.getDate());
		if(appointmentManager.getAll().get().anyMatch(appointment -> appointment.getDate().isEqual(localDate))){
			error.getMessage();
		}else{
			appointmentManager.createAppointment(form);
		}

		return "redirect:/createAppointment";
	}

	@GetMapping("/listOfApps")
	String listOfAppointments(Model model){
		model.addAttribute("ListOfApps", appointmentManager.getAll());
		return "listofApps";
	}


}
