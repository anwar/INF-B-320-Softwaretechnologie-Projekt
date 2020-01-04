package kleingarten.application;

import kleingarten.tenant.TenantManager;
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

	public ApplicationController(ApplicationManager manager, TenantManager tenantManager) {
		this.tenantManager = tenantManager;
		this.manager = manager;

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

		manager.accept(manager.getById(Long.parseLong(appId)));

		String password = tenantManager.generatedPassword(8);

		System.out.println("Ihr Password ist: " + password);

		tenantManager.createNewTenant(manager.getById(Long.parseLong(appId)).getFirstName(), manager.getById(Long.parseLong(appId)).getLastName(), manager.getById(Long.parseLong(appId)).getEmail(), password);

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
