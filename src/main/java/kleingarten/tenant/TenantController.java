package kleingarten.tenant;


import com.mysema.commons.lang.Assert;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
class TenantController {
	private final TenantManager tenantManager;
	private final TenantRepository tenantRepository;
	private final TenantService tenantService;

	/**
	 * Constructor of class {@link TenantController}
	 *
	 * @param tenantManager    manager class {@link TenantManager} for managing {@link Tenant}s
	 * @param tenantRepository repository of tenants as {@link TenantRepository}
	 * @param tenantService    service class {@link TenantService}
	 */
	TenantController(TenantManager tenantManager, TenantRepository tenantRepository, TenantService tenantService) {
		Assert.notNull(tenantManager, "TenantManager must not be null");
		this.tenantManager = tenantManager;
		this.tenantRepository = tenantRepository;
		this.tenantService = tenantService;
	}

	/**
	 * Shows an overview of {@link Tenant} as a list
	 *
	 * @param model view to show list of {@link Tenant}s as {@link Model}
	 * @return html to show list of {@link Tenant}s
	 */
	@GetMapping("/tenants")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String tenants(Model model) {
		model.addAttribute("tenantList", tenantManager.findEnabled());
		return "tenant/tenants";
	}

	@GetMapping("/pretenants")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String pretenants(Model model){
		model.addAttribute("preTenantList", tenantManager.findDisabled());
		return "tenant/pretenants";
	}

	@GetMapping("/deactivatetenant")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String deactivatetenant(@RequestParam("id") String id){
		System.out.println(tenantManager.get(Long.parseLong(id)).getForename());
		System.out.println(tenantManager.get(Long.parseLong(id)).getUserAccount().isEnabled());
		tenantService.makePreTenant(Long.parseLong(id));
		System.out.println(tenantManager.get(Long.parseLong(id)).getUserAccount().isEnabled());
		return "redirect:/tenants";
	}

	/**
	 * Shows the details of a specific {@link Tenant}
	 *
	 * @param id    identifier of {@link Tenant} to show the details
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
	 * View to edit a {@link Tenant}s details
	 *
	 * @param id    identifier of {@link Tenant} to edit the details
	 * @param model view for editing a {@link Tenant} as {@link Model}
	 * @return html for editing a {@link Tenant}
	 */
	@GetMapping("/modifyTenant")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String modifyTenant(@RequestParam("id") Long id, Model model) {
		model.addAttribute("tenant", tenantManager.get(id));
		model.addAttribute("roles", TenantRole.getRoleList());
		return "tenant/modifyTenant";
	}

	/**
	 * Getter for the changed details of a {@link Tenant} to save in the {@link Tenant}
	 *
	 * @param id        identifier of {@link Tenant} as {@link Long}
	 * @param forename  input forename as {@link String}
	 * @param surname   input surname as {@link String}
	 * @param phone     input phone number as {@link String}
	 * @param email     input email as {@link String}
	 * @param address   input address as {@link String}
	 * @param birthdate input birth date as {@link String}
	 * @param roles     input role as {@link String}
	 * @return html of the {@link Tenant}s html with saved new details of {@link Tenant}
	 */
	@PostMapping("/modifiedTenant")
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String modifiedTenant(@RequestParam("id") Long id,
						  @RequestParam("forename") String forename,
						  @RequestParam("surname") String surname,
						  @RequestParam("phone") String phone,
						  @RequestParam("email") String email,
						  @RequestParam("address") String address,
						  @RequestParam("birthdate") String birthdate,
						  @RequestParam("role") String roles) {

		tenantManager.get(id).setForename(forename);
		tenantManager.get(id).setSurname(surname);
		tenantManager.get(id).setPhonenumber(phone);
		tenantManager.get(id).getUserAccount().setEmail(email);
		tenantManager.get(id).setAddress(address);
		tenantManager.get(id).setBirthdate(birthdate);
		tenantManager.get(id).deleteRoles();
		for (String role : roles.split(",")) {
			if (TenantRole.isUnique(role)) {
				for (Tenant t : tenantManager.findByRole(Role.of(role))) {
					t.removeRole(Role.of(role));
				}
			}
			if (role.equals("Obmann")) {
				Random randomColorGenerator = new Random();
				int color = randomColorGenerator.nextInt(0x1000000);
				tenantManager.get(id).setChairmanColor(String.format("#%06X", color));
			}
			tenantManager.get(id).addRole(Role.of(role));
		}
		tenantRepository.save(tenantManager.get(id));

		return "redirect:/tenants";
	}

	/**
	 * View to change the password of a logged in {@link Tenant}
	 *
	 * @return html to change the password
	 */
	@PreAuthorize("hasRole('Hauptpächter') || hasRole('Nebenpächter')")
	@GetMapping("/changePassword")
	String changePassword() {
		return "tenant/changePassword";
	}

	/**
	 * Getter to save the changed password in {@link Tenant}
	 *
	 * @param userAccount      {@link UserAccount} of logged in {@link Tenant} as {@link String}
	 * @param oldPassword      of the {@link Tenant} as {@link String}
	 * @param newPassword      of the {@link Tenant} as {@link String}
	 * @param repeatedPassword checks if the {@link Tenant} had a typo in their new password
	 * @return html to change the password
	 */
	@PostMapping("/changedPassword")
	String changedPassword(@LoggedIn UserAccount userAccount, @RequestParam("old") String oldPassword,
						   @RequestParam("new") String newPassword, @RequestParam("repeat") String repeatedPassword) {
		tenantService.changePassword(userAccount, oldPassword, newPassword, repeatedPassword);

		return "tenant/successPassword";
	}

	/**
	 * View to change the email of a logged in {@link Tenant}
	 *
	 * @param userAccount {@link UserAccount} of logged in {@link Tenant}
	 * @param model       to show the email of {@link Tenant}
	 * @return html to change the email
	 */
	@GetMapping("/changeEmail")
	String changeEmail(@LoggedIn UserAccount userAccount, Model model) {
		model.addAttribute("email", userAccount.getEmail());
		return "tenant/changeEmail";
	}

	/**
	 * Getter to save the new Email of a {@link Tenant}
	 *
	 * @param userAccount   {@link UserAccount} of {@link Tenant} to change their email
	 * @param oldEmail      of {@link Tenant} as {@link String}
	 * @param newEmail      of {@link Tenant} as {@link String}
	 * @param repeatedEmail to check if {@link Tenant} had a typo in their new email
	 * @return view to change the email
	 */
	@PostMapping("/changedEmail")
	String changedEmail(@LoggedIn UserAccount userAccount, @RequestParam("old") String oldEmail,
						@RequestParam("new") String newEmail, @RequestParam("repeat") String repeatedEmail) {
		tenantService.changeEmail(tenantManager.getTenantByUserAccount(userAccount).getId(), oldEmail, newEmail,
				repeatedEmail);
		return "/tenant/successEMail";
	}

	/**
	 * @return View to register a new {@link Tenant} as {@link String}
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@GetMapping("/register")
	String register() {
		return "tenant/register";
	}

	/**
	 * Getter to save the new registered {@link Tenant}
	 *
	 * @param forename  of the new {@link Tenant} as {@link String}
	 * @param surname   of the new {@link Tenant} as {@link String}
	 * @param birthdate of the new {@link Tenant} as {@link String}
	 * @param address   of the new {@link Tenant} as {@link String}
	 * @param email     of the new {@link Tenant} as {@link String}
	 * @param phone     of the new {@link Tenant} as {@link String}
	 * @return html of the {@link Tenant} overview
	 */
	@PostMapping("/registered")
	String registered(@RequestParam("forename") String forename, @RequestParam("surname") String surname,
					  @RequestParam("birthdate") String birthdate, @RequestParam("address") String address,
					  @RequestParam("email") String email, @RequestParam("phone") String phone) {
		tenantManager.createNewPerson(forename, surname, address, phone, birthdate, email);

		return ("redirect:/tenants");
	}
}
