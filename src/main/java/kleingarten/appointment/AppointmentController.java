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

	@GetMapping("/createAForm")
	String Appointments (Model model, CreateAppointmentForm form){
		model.addAttribute("dateNow", LocalDate.now());
		model.addAttribute("timeNow", LocalTime.now());
		model.addAttribute("form", form);
		return "createAForm";
	}

	@PostMapping("/createAForm")
	String addAppointment(@Valid CreateAppointmentForm form, Errors result){

		appointmentManager.createAppointment(form);
		return "redirect:/createAForm";
	}

	@GetMapping("/listOfApps")
	String listOfAppointments(Model model){
		model.addAttribute("ListOfApps", appointmentManager.getAll());
		return "listofApps";
	}


}
