package kleingarten.application;

import kleingarten.message.MessageService;
import kleingarten.tenant.TenantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ApplicationController {

	private final ApplicationManager manager;
	private final TenantManager tenantManager;
	private MessageService messageService;

	public ApplicationController(ApplicationManager manager,
								 TenantManager tenantManager,
								 @Autowired MessageService messageService) {
		this.tenantManager = tenantManager;
		this.manager = manager;
		this.messageService = messageService;
	}

	@GetMapping("/createApplication/{plotId}")
	public String addPlotToModel(Model model, CreateApplicationForm form, @PathVariable String plotId) {

		System.out.println("ID  " + plotId);
		model.addAttribute("plotId", plotId);
		form.setPlotId(plotId);
		model.addAttribute("form", form);
		return "application/createApplication";
	}

	@PostMapping("/createApplication")
	public String addApplication(@Valid CreateApplicationForm form, Error error) {

		System.out.println("new Application!!! : " + form.getFirstName() + " " + form.getLastName() + " - " + form.getEmail());
		Application application = new Application(form.getFirstName(), form.getLastName(), form.getEmail(), form.getPlotId());
		manager.add(application);

		manager.printAllToConsole();

		return "redirect:/plot/" + form.getPlotId();
	}

	@GetMapping("/showApplications/{plotId}")
	public String showApplications(Model model, @PathVariable String plotId) {

		model.addAttribute("plotId", plotId);

		List<Application> list = manager.getByPlotId(plotId);

		model.addAttribute("applications", Streamable.of(list));

		return "application/activeApplications";
	}

	@PostMapping("/accept")
	public String acceptApplication(@RequestParam String plotId, @RequestParam String appId) {

		System.out.println(plotId + " - " + appId);

		Application app = manager.getById(Long.parseLong(appId));
		manager.accept(app);

		String password = tenantManager.generatedPassword(8);
		String passMessageStr = "Ihr Benuterzerkonto Passwort ist: " + password;

		tenantManager.createNewTenant(app.getFirstName(), app.getLastName(), app.getEmail(), password);

		System.out.println(passMessageStr);
		messageService.sendMessage(app.getEmail(), "Passwort für Benutzerkonto", passMessageStr);

		return "redirect:/showApplications/" + plotId;
	}

	@PostMapping("/deny")
	public String denyApplication(@RequestParam String plotId, @RequestParam String appId) {

		System.out.println(plotId + " - " + appId);

		Application app = manager.getById(Long.parseLong(appId));
		app.deny();

		manager.save(app);

		return "redirect:/showApplications/" + plotId;
	}

}
