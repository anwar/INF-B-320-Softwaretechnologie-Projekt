package kleingarten.application;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import kleingarten.appointment.CreateWorkAssignmentForm;
import kleingarten.plot.Plot;

@Controller
public class ApplicationController {
	
	private final ApplicationManager manager;
	
	public ApplicationController(ApplicationManager manager) {
		
		this.manager = manager;
		
	}
	
	@GetMapping("/createApplication/{plotId}")
	public String addPlotToModel(Model model, CreateApplicationForm form, @PathVariable String plotId){
		
		System.out.println("ID  "+plotId);
		model.addAttribute("plotId", plotId);
		form.setPlotId(plotId);
		model.addAttribute("form", form);
		return "application/createApplication";
	}
	
	@PostMapping("/createApplication")
	public String addApplication(@Valid CreateApplicationForm form, Error error) {
		
		System.out.println("new Application!!! : "+form.getFirstName() + " " + form.getLastName() + " - "+form.getEmail());
		Application application = new Application(form.getFirstName(), form.getLastName(), form.getEmail(), form.getPlotId());
		manager.add(application);
		
		manager.printAllToConsole();
		
		return "redirect:/plot/"+form.getPlotId();
	}

}
