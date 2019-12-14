package kleingarten.tenant;


import com.mysema.commons.lang.Assert;
import org.salespointframework.useraccount.*;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.desktop.SystemSleepEvent;
import java.util.Arrays;
import java.util.List;

@Controller
class TenantController {
	private final TenantManager tenantManager;
	private final TenantRepository tenantRepository;
	private final TenantService tenantService;
	private final AuthenticationManager authenticationManager;

	/**
	 * Constructor of class {@link TenantController}
	 * @param tenantManager manager class {@link TenantManager} for managing {@link Tenant}s
	 * @param tenantRepository repository of tenants as {@link TenantRepository}
	 * @param tenantService service class {@link TenantService}
	 */
	TenantController(TenantManager tenantManager, TenantRepository tenantRepository, TenantService tenantService, AuthenticationManager authenticationManager) {
		Assert.notNull(tenantManager, "TenantManager must not be null");
		this.tenantManager = tenantManager;
		this.tenantRepository = tenantRepository;
		this.tenantService = tenantService;
		this.authenticationManager = authenticationManager;
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
		model.addAttribute("roles", TenantRole.getRoleList());
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
						  @RequestParam("birthdate") String birthdate,
						   @RequestParam("role") String roles){

		tenantManager.get(id).setForename(forename);
		tenantManager.get(id).setSurname(surname);
		tenantManager.get(id).setPhonenumber(phone);
		tenantManager.get(id).getUserAccount().setEmail(email);
		tenantManager.get(id).setAddress(address);
		tenantManager.get(id).setBirthdate(birthdate);
		tenantManager.get(id).deleteRoles();
		for(String role : roles.split(",")){
			if(TenantRole.isUnique(role)){
				for (Tenant t : tenantManager.findByRole(Role.of(role))){
					t.removeRole(Role.of(role));
				}
			}
			tenantManager.get(id).addRole(Role.of(role));
		}
		tenantRepository.save(tenantManager.get(id));

		return "redirect:/tenants";
	}
	@PreAuthorize("hasRole('Hauptpächter') || hasRole('Nebenpächter')")
	@GetMapping("/changePassword")
	String changePassword(@LoggedIn UserAccount userAccount, Model model){
		// model.addAttribute("password", userAccount.getPassword().toString());
		//model.addAttribute("password", authenticationManager.getCurrentUser().get().getPassword().);
		return"/tenant/changePassword";
	}

	@PostMapping("/changedPassword")
	String changedPassword(@LoggedIn UserAccount userAccount, @RequestParam("old") String oldPassword, @RequestParam("new") String newPassword,
						   @RequestParam("repeat") String repeatedPassword){
		tenantService.changePassword(userAccount, oldPassword, newPassword, repeatedPassword);

		return "redirect:/changePassword";
	}

	@GetMapping("/changeEmail")
	String changeEmail(@LoggedIn UserAccount userAccount, Model model){
		model.addAttribute("email", userAccount.getEmail());
		return"/tenant/changeEmail";
	}

	@PostMapping("/changedEmail")
	String changedEmail(@LoggedIn UserAccount userAccount, @RequestParam("old") String oldEmail, @RequestParam("new") String newEmai,
						@RequestParam("repeat") String repeatedEmail){
		tenantService.changeEmail(tenantManager.getTenantByUserAccount(userAccount).getId(), oldEmail, newEmai, repeatedEmail);
		return "redirect:/changeEmail";
	}

	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@GetMapping("/register")
	String register(){
		return "/tenant/register";
	}

	@PostMapping("/registered")
	String registered(@RequestParam("forename") String forename, @RequestParam("surname") String surname, @RequestParam("birthdate") String birthdate,
					  @RequestParam("address") String address, @RequestParam("email") String email, @RequestParam("phone") String phone){
		tenantManager.createNewPerson(forename, surname, address, phone, birthdate, email);

		return ("redirect:/tenants");
	}
}
