package kleingarten.tenant;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class TenantController {
	private final TenantManager tenantManager;

	//private final TenantRepository tenantRepository;
	public TenantController(TenantManager tenantManager){
		Assert.notNull(tenantManager, "TenantManager must not be null!");
		this.tenantManager = tenantManager;

	}


	@GetMapping("/tenants")
	@PreAuthorize("hasRole('ROLE_VORSTAND')")
	String tenants(Model model){
		model.addAttribute("tenantList", tenantManager.getAll());
		System.out.println(tenantManager.get(Long.valueOf(1)));
		return "tenants";
	}

	@GetMapping("/tenantdetails")
	@PreAuthorize("hasRole('ROLE_VORSTAND')")
	String tenantdetails(@RequestParam("id") String id, Model model){
		model.addAttribute("tenant", tenantManager.get(Long.parseLong(id)));
		System.out.println(id);
		System.out.println(tenantManager.get(Long.parseLong(id)).getUserAccount());

		return "tenantdetails";
	}

	@GetMapping("/complains")
	String complains(){
		return "complains";
	}

	@PostMapping("/register")
	String registerNew(@Valid RegistrationForm form, Errors result){
		if(result.hasErrors()){
			return "register";
		}

		tenantManager.createTenant(form);
		return "home";
	}

	@GetMapping("/register")
	String register(Model model, RegistrationForm form){
		model.addAttribute("form", form);
		return "register";}

	@GetMapping("/modifyTenant")
	String modifyTenant(Model model, RegistrationForm form){
		model.addAttribute("form", form);
		return "modifyTenant";

	}

	@GetMapping("/tenantOverview")
	String tenantOverview(){
		return "tenantOverview";
	}


	}


