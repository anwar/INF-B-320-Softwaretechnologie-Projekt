package kleingarten.tenant;

import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class TenantManager {

	private final TenantRepository tenants;
	public UserAccountManager userAccounts;

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
}
