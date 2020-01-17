package kleingarten.tenant;

import kleingarten.plotManagement.Plot;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;


@Component
public class TenantDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(TenantDataInitializer.class);

	private final UserAccountManager userAccountManager;
	private final TenantRepository tenantRepository;

	TenantDataInitializer(UserAccountManager userAccountManager, TenantRepository tenantRepository){

		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(tenantRepository, "TenantRepository must not be null!");

		this.tenantRepository = tenantRepository;
		this.userAccountManager = userAccountManager;
	}

	@Override
	public void initialize(){

		if(userAccountManager.findByUsername("vorstandsvorsitzender").isPresent()){
			return;
		}

		LOG.info("Creating default users and customers");
		LOG.info("Habe fertig");

		var password = Password.UnencryptedPassword.of("123");

		var vorstandRole = Role.of("ROLE_VORSTAND");
		var obmannRole = Role.of("ROLE_VORSTAND");
		var financeRole = Role.of("ROLE_FINANZEN");
		var stellvertreterRole = Role.of("ROLE_STELLVERTRETER");

		var bossAccount = userAccountManager.create("vorstandsvorsitzender", password, vorstandRole);
		userAccountManager.save(bossAccount);

		var vorstandsvorsitzender = new Tenant("Peter Klaus", "Am Berg 5, 12423 Schiessmich Tot", "peter@klaus.haha", "1342534654", "13.4.1999", bossAccount);
		tenantRepository.saveAll(List.of(vorstandsvorsitzender));

		var tenantRole = Role.of("ROLE_TENANT");

		var abc = userAccountManager.create("tenant", password, tenantRole);

		var abc1 = new Tenant("Franziska Kiel", "bei Isa", "francys@email.com", "12342544536", "19.08.1998", abc);


		tenantRepository.saveAll(List.of(abc1));

	}
}
