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

	public Tenant changeRoles(Role role, Role newRole,Tenant tenant){
		tenant.getUserAccount().remove(role);
		tenant.getUserAccount().add(newRole);
		return tenant;
	}

	public Tenant getTenantByUserAccount(UserAccount userAccount){
		if (tenants.findByUserAccount(userAccount).isEmpty()){
			throw new IllegalArgumentException("Tenant not found");
		} else {
			return tenants.findByUserAccount(userAccount).get();
		}
	}

	public boolean tenantHasRole(Tenant tenant, Role role){
		return tenant.getUserAccount().hasRole(role);
	}

	public Streamable<Tenant> findEnabled(){
		return tenants.findAll().filter(c -> c.getUserAccount().isEnabled());
	}

	public  Streamable<Tenant> findDisabled(){
		return  tenants.findAll().filter(c -> !c.getUserAccount().isEnabled());
	}
}
