package kleingarten.tenant;


import com.mysema.commons.lang.Assert;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class TenantController {
	private final TenantManager tenantManager;
	private final TenantRepository tenantRepository;

	TenantController(TenantManager tenantManager, TenantRepository tenantRepository) {
		Assert.notNull(tenantManager, "TenantManager must not be null");
		this.tenantManager = tenantManager;
		this.tenantRepository = tenantRepository;
	}

	@GetMapping("/tenants")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String tenants(Model model) {
		model.addAttribute("tenantList", tenantManager.getAll());
		return "tenant/tenants";
	}

	@GetMapping("/tenantDetails")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String tenantDetails(@RequestParam("id") String id, Model model) {
		model.addAttribute("tenant", tenantManager.get(Long.parseLong(id)));
		return "tenant/tenantDetails";
	}

	@GetMapping("/modifyTenant")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String modifyTenant(@RequestParam("id") Long id, Model model){
		model.addAttribute("tenant", tenantManager.get(id));
		return "modifyTenant";
	}

	@PostMapping("/modifiedTenant")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String modifiedTenant( @RequestParam("id")		  Long id,
						   @RequestParam("forename") String forename,
						  @RequestParam("surname")  String surname,
						  @RequestParam("phone")     String phone,
						  @RequestParam("email")     String email,
						  @RequestParam("address")   String address,
						  @RequestParam("birthdate") String birthdate){

		tenantManager.get(id).setForename(forename);
		tenantManager.get(id).setSurname(surname);
		tenantManager.get(id).setPhonenumber(phone);
		tenantManager.get(id).getUserAccount().setEmail(email);
		tenantManager.get(id).setAddress(address);
		tenantManager.get(id).setBirthdate(birthdate);

		tenantRepository.save(tenantManager.get(id));

		return "redirect:/tenants";
	}

}
