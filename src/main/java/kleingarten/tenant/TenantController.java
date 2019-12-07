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

	/**
	 * Constructor of class {@link TenantController}
	 * @param tenantManager manager class {@link TenantManager} for managing {@link Tenant}s
	 * @param tenantRepository repository of tenants as {@link TenantRepository}
	 */
	TenantController(TenantManager tenantManager, TenantRepository tenantRepository) {
		Assert.notNull(tenantManager, "TenantManager must not be null");
		this.tenantManager = tenantManager;
		this.tenantRepository = tenantRepository;
	}

	/**
	 * Shows an overview of {@link Tenant} as a list
	 * @param model view to show list of {@link Tenant}s as {@link Model}
	 * @return html to show list of {@link Tenant}s
	 */
	@GetMapping("/tenants")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String tenants(Model model) {
		model.addAttribute("tenantList", tenantManager.getAll());
		return "tenant/tenants";
	}

	/**
	 * Shows the details of a specific {@link Tenant}
	 * @param id identifier of {@link Tenant} to show the details
	 * @param model view to show the details of {@link Tenant} as {@link Model}
	 * @return html view to show the details of {@link Tenant}
	 */
	@GetMapping("/tenantDetails")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String tenantDetails(@RequestParam("id") String id, Model model) {
		model.addAttribute("tenant", tenantManager.get(Long.parseLong(id)));
		return "tenant/tenantDetails";
	}

	/**
	 * view to edit a {@link Tenant}s details
	 * @param id identifier of {@link Tenant} to edit the details
	 * @param model view for editing a {@link Tenant} as {@link Model}
	 * @return html for editing a {@link Tenant}
	 */
	@GetMapping("/modifyTenant")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String modifyTenant(@RequestParam("id") Long id, Model model){
		model.addAttribute("tenant", tenantManager.get(id));
		return "tenant/modifyTenant";
	}

	/**
	 * Getter for the changed details of a {@link Tenant} to save in the {@link Tenant}
	 * @param id identifier of {@link Tenant}
	 * @param forename input forename as String
	 * @param surname input surname as String
	 * @param phone input phone number as String
	 * @param email input email as String
	 * @param address input address as String
	 * @param birthdate input birth date as String
	 * @return html of the tenants html with saved new details of {@link Tenant}
	 */
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

	@GetMapping("/changePassword")
	String changePassword(){
		return"/tenant/changePassword";
	}
	@GetMapping("/changeEmail")
	String changeEmail(){
		return"/tenant/changeEmail";
	}
}
