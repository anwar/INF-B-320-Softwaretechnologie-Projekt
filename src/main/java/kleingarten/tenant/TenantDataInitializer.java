package kleingarten.tenant;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
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
		LOG.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(tenantRepository, "TenantRepository must not be null");

		this.tenantRepository = tenantRepository;
		this.userAccountManager = userAccountManager;
	}

	@Override
	public void initialize() {
		LOG.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		if(userAccountManager.findByUsername("peter.klaus").isPresent()){
			return;
		}


		LOG.info("Creating default users and customers");
		LOG.info("Habe fertig");

		var password = Password.UnencryptedPassword.of("123");

		var vorstandRole = Role.of("ROLE_VORSTAND");
		var stellvertreterRole = Role.of("ROLE_STELLVERTRETER");
		var obmannRole = Role.of("ROLE_OBMANN");
		var financeRole = Role.of("ROLE_KASSIERER");
		var tenantRole = Role.of("ROLE_TENANT");

		Tenant boss = new Tenant("Peter", "Klaus", "Am Berg 5, 12423 Irgendwo im Nirgendwo", "peter.klaus@email.com", "01242354356", "13.04.1999",
			userAccountManager.create("peter.klaus", password, tenantRole), vorstandRole);


		Tenant obmann = new Tenant("Hubert", "Grumpel", "Hinter den 7 Bergen, 98766 Zwergenhausen",
			"hubert.grumpel2@cloud.com", "012345678", "04.09.1978",
			userAccountManager.create("hubertgrumpel", password, tenantRole), tenantRole);

		obmann.addRole(obmannRole);
		boss.addRole(vorstandRole);

		tenantRepository.saveAll(List.of(boss, obmann));
	}
}
