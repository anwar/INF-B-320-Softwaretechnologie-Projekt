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

		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(tenantRepository, "TenantRepository must not be null");

		this.tenantRepository = tenantRepository;
		this.userAccountManager = userAccountManager;
	}

	@Override
	public void initialize() {

		if(userAccountManager.findByUsername("peter.klaus").isPresent()){
			return;
		}


		LOG.info("Creating default users and customers");
		LOG.info("Habe fertig");

		var password = Password.UnencryptedPassword.of("123");

		var vorstandRole = Role.of("Vorstandsvorsitzender");
		var protocolRole = Role.of("Protokollant");
		var stellvertreterRole = Role.of("Stellvertreter");
		var obmannRole = Role.of("Obmann");
		var financeRole = Role.of("Kassierer");
		var maintenantRole = Role.of("Hauptpächter");
		var subtenantRole = Role.of("Nebenpächter");
		var waterRole = Role.of("Wassermann");

		Tenant boss = new Tenant("Peter", "Klaus", "Am Berg 5, 12423 Irgendwo im Nirgendwo", "peter.klaus@email.com",
			"01242354356", "13.04.1999",
			userAccountManager.create("peter.klaus", password, maintenantRole));


		Tenant obmann = new Tenant("Hubert", "Grumpel", "Hinter den 7 Bergen, 98766 Zwergenhausen",
			"hubert.grumpel2@cloud.com", "012345678", "04.09.1978",
			userAccountManager.create("hubertgrumpel", password, maintenantRole));

		Tenant cashier = new Tenant("Bill", "Richart", "Am Bahnhof 25, 07875 Dorfdorf", "billy,billbill@geld.com",
			"0123098874326", "13.05.1968", userAccountManager.create("bill", password, subtenantRole));

		Tenant replacement = new Tenant("Sophie", "Kirmse", "Am Teichplatz 5, 67807 Meldetsichnie",
			"s.krimse@gemaile.com", "034567892132", "08.12.1988",
			userAccountManager.create("sophie", password, subtenantRole));

		Tenant protocol = new Tenant("Franziska", "Kiel", "Bei Isa", "francys@email.com",
			"0896548786890", "19.08.1998", userAccountManager.create("franziska", password, maintenantRole));

		Tenant waterman = new Tenant("Atlas", "Neptunius", "An der Promenade 34, 01298 Atlantis",
			"neptuns.bart@fishmail.com", "0980790789","08.09.1567", userAccountManager.create("neptun", password, maintenantRole));

		obmann.addRole(obmannRole);
		boss.addRole(vorstandRole);
		cashier.addRole(financeRole);
		replacement.addRole(stellvertreterRole);
		protocol.addRole(protocolRole);
		waterman.addRole(waterRole);

		tenantRepository.saveAll(List.of(boss, obmann, cashier, replacement, protocol, waterman));
	}
}
