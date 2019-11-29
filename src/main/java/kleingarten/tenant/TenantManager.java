package kleingarten.tenant;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class TenantManager {

	private final TenantRepository tenants;
	public UserAccountManager userAccounts;

	@Autowired
	TenantManager(TenantRepository tenants, UserAccountManager userAccounts){

		Assert.notNull(tenants, "TenantRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManager must not be null!");

		this.userAccounts = userAccounts;
		this.tenants = tenants;

	}

	public Streamable<Tenant> getAll(){
		return tenants.findAll();
	}

	public Tenant get(Long id){
		return tenants.findById(id).orElse(null);
	}

	public Tenant getTenantByUserAccount(UserAccount userAccount){
		var a = tenants.findAll().filter(c -> c.getUserAccount().getLastname().equals(userAccount.getLastname()));
		return a.toList().get(0);
	}

	public boolean tenantHasRole(Tenant tenant, Role role){
		return tenant.getRoles().equals(role);
	}

	public Streamable<Tenant> findEnabled(){
		return tenants.findAll().filter(c -> c.getUserAccount().isEnabled());
	}

	public  Streamable<Tenant> findDisabled(){
		return  tenants.findAll().filter(c -> !c.getUserAccount().isEnabled());
	}
}
