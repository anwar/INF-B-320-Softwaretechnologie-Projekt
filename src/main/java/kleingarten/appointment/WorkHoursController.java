package kleingarten.appointment;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.SalespointIdentifier;
import org.salespointframework.order.OrderIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class WorkHoursController {

	private AppointmentManager appointmentManager;
	private WorkHoursManager workHoursManager;

	public WorkHoursController(AppointmentManager appointmentManager, WorkHoursManager workHoursManager){
		this.appointmentManager = appointmentManager;
		this.workHoursManager = workHoursManager;
	}

	@GetMapping("/addAppointment/{plotID}")
	public String appointmentList(Model model, @PathVariable SalespointIdentifier plotID){
		model.addAttribute("ListofAppointments", appointmentManager.getAll());
		//model.addAttribute("ListOfTimes", appointmentManager.getAll().get().m
		//selected machen das man auf ausgewählte datum die Zeiten bekommt.
		return "addAppointment";
	}


	@PostMapping("/addAppointment{plotID}")
	public String addAppointment(AddAppointmentForm form, @PathVariable SalespointIdentifier plotID){
		workHoursManager.addAppointment(form, plotID);
		return "redirect:/myPlot";
	}
}
