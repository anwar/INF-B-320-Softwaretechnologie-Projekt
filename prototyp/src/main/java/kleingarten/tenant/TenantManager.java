package kleingarten.tenant;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
public class TenantManager {

	private final TenantRepository tenants;
	public UserAccountManager userAccounts;


	TenantManager(TenantRepository tenants, UserAccountManager userAccounts) {

		Assert.notNull(tenants, "TenantRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManager must not be nnull!");

		this.userAccounts = userAccounts;
		this.tenants = tenants;
	}

	public Streamable<Tenant> getAll() {
		return tenants.findAll();
	}

	public Streamable<Tenant> findEnabled(){
		return getAll().filter(c -> c.getUserAccount().isEnabled());
	}

	public Streamable<Tenant> findDisabled(){
		return getAll().filter(c -> !c.getUserAccount().isEnabled());
	}

	public Tenant get(Long id) {
		return tenants.findById(id).orElse(null);
	}

	public Tenant createTenant(RegistrationForm form) {

		Assert.notNull(form, "Registration form must not be mull");

		var password = Password.UnencryptedPassword.of(form.getName());
		var userAccount = userAccounts.create(form.getName(), password);

		return tenants.save((new Tenant(form.getName(), form.getAddress(), form.getEmail(),form.getPhonenumber(), form.getBirthdate(), userAccount)));

	}

	public Role getRole(UserAccount userAccount){
		return userAccount.getRoles().toList().get(0);
	}

	public void changeUserRole(UserAccount userAccount){
		userAccount.remove(getRole(userAccount));
		//userAccount.add()

	}

	//public Tenant addTenant(RegistrationForm form){

	//}

}
