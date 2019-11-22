package kleingarten.tenant;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class TenantDataInitialzier implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(TenantDataInitialzier.class);
	private final UserAccountManager userAccountManager;
	private final TenantRepository tenantRepository;

	TenantDataInitialzier(UserAccountManager userAccountManager, TenantRepository tenantRepository){
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(tenantRepository, "TenantRepository must not be null");

		this.tenantRepository = tenantRepository;
		this.userAccountManager = userAccountManager;
	}

	@Override
	public void initialize() {

		if(userAccountManager.findByUsername("vorstandsvorsitzender").isPresent()){
			return;
		}

		LOG.info("Creating default users and customers");
		LOG.info("Habe fertig");

		var password = Password.UnencryptedPassword.of("123");

		var vorstandRole = Role.of("Role_VORSTAND");
	}
}
