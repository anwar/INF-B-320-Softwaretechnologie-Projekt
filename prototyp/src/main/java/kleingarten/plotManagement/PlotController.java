package kleingarten.plotManagement;


import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class PlotController {

	private final PlotService plotService;
	private final TenantManager tenantManager;


	public PlotController(PlotService plotService, TenantManager tenantManager){
		this.plotService = plotService;
		this.tenantManager = tenantManager;
	}

	@GetMapping("/anlage")
	String plots(Model model){
		return "anlage";
	}

	@GetMapping("/freePlots")
	String freePlots(Model model){
		model.addAttribute("freePlots", plotService.getAll().filter(plot -> plot.getStatus().equals(PlotStatus.FREE)));
		model.addAttribute("takenPlots", plotService.getAll().filter(plot -> plot.getStatus().equals(PlotStatus.TAKEN)));
		return "freePlots";
	}

	@GetMapping("/plotOverview")
	String plotOverview(Model model){

		model.addAttribute("plotList", plotService.getAll());
		model.addAttribute("tenantList", tenantManager.getAll());

		return "plotOverview";
	}

	@PostMapping("/plotOverview")
	String updatePlot(String plotName, String tenantID) {

		Plot plot = plotService.get(plotName);


		if (tenantID != null) {
			Plot requestedPlot = plotService.get(plotName);
			Tenant requestedTenant = tenantManager.get(Long.parseLong(tenantID));
			this.plotService.updateTenant(requestedPlot, requestedTenant);
		}
		return "redirect:/plotOverview";
	}

	@GetMapping("/createPlot")
	@PreAuthorize("hasRole('ROLE_VORSTAND')")
	String register(Model model, PlotRegistrationForm form){
		model.addAttribute("form", form);
		return "createPlot";
	}

	@PostMapping("/createPlot")
	String registerNew(@Valid @ModelAttribute("form") PlotRegistrationForm form) {

		plotService.createPlot(form);

		return "redirect:/plotOverview";
	}


}
