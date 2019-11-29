package kleingarten.tenant;


import com.mysema.commons.lang.Assert;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class TenantController {
	private final TenantManager tenantManager;
	private final TenantRepository tenantRepository;


	TenantController(TenantManager tenantManager, TenantRepository tenantRepository){
		Assert.notNull(tenantManager,"TenantManager must not be null");
		this.tenantManager = tenantManager;
		this.tenantRepository = tenantRepository;
	}

	@GetMapping("/tenants")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String tenants(Model model){
		model.addAttribute("tenantList", tenantManager.getAll());
		return "tenants";
	}

	@GetMapping("/tenantDetails")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String tenantDetails(@RequestParam("id") String id, Model model){
		model.addAttribute("tenant", tenantManager.get(Long.parseLong(id)));
		return "tenantDetails";
	}
	@GetMapping("/myPlot")
	String tenantOverview(){
		return "myPlot";
	}
}
