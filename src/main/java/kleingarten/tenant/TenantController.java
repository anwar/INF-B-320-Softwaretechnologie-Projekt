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
	private final UserAccountManager userAccountManager;


	TenantController(TenantManager tenantManager, TenantRepository tenantRepository, UserAccountManager userAccountManager){
		this.userAccountManager = userAccountManager;
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
						  @RequestParam("birthdate") String birthdate,
						  Model model){

		String newForeName;
		String newSurname;
		String newEmail;
		String newAddress;
		String newPhone;
		String newBirthdate;

		if (!forename.equals(tenantManager.get(id).getForename())){
			newForeName = forename;
		} else {
			newForeName = tenantManager.get(id).getForename();
;		}
		if (!surname.equals(tenantManager.get(id).getSurname())){
			newSurname = surname;
		} else {
			newSurname = tenantManager.get(id).getSurname();
		}
		if (!email.equals(tenantManager.get(id).getUserAccount().getEmail())){
			newEmail = email;
			tenantManager.get(id).getUserAccount().setEmail(newEmail);
		} else {
			newEmail = tenantManager.get(id).getUserAccount().getEmail();
		}
		if (!address.equals(tenantManager.get(id).getAddress())){
			newAddress = address;
		} else{
			newAddress = tenantManager.get(id).getEmail();
		}
		if (!phone.equals(tenantManager.get(id).getPhonenumber())){
			newPhone = phone;
		} else {
			newPhone = tenantManager.get(id).getPhonenumber();
		}
		if (!birthdate.equals(tenantManager.get(id).getBirthdate())){
			newBirthdate = birthdate;
		} else {
			newBirthdate = tenantManager.get(id).getBirthdate();
		}

		Tenant tenant = new Tenant(newForeName, newSurname, newAddress, newPhone, newBirthdate,
			userAccountManager.create("peterklaus1", Password.UnencryptedPassword.of(tenantManager.get(id).getUserAccount().getPassword().toString()),
			newEmail, tenantManager.get(id).getUserAccount().getRoles().iterator().next()));
		tenant.setId(id);

		tenantRepository.save(tenant);

		return "redirect:/tenants";
	}

}
