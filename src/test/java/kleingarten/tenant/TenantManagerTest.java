package kleingarten.tenant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;


import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class TenantManagerTest {
	private final UserAccountManager userAccountManager;
	private final TenantManager tenantManager;
	private final TenantRepository tenantRepository;

	private Tenant tenant;

	public TenantManagerTest(@Autowired UserAccountManager userAccountManager, @Autowired TenantManager tenantManager, @Autowired TenantRepository tenantRepository) {
		this.userAccountManager = userAccountManager;
		this.tenantManager = tenantManager;
		this.tenantRepository = tenantRepository;
	}

//	@BeforeEach
//	void SetUp(){
//		tenant = new Tenant("Jassi", "Gepackert", "Neben Isa und Francy",
//			"908964875734", "13.05.1999", userAccountManager.create("jassi", Password.UnencryptedPassword.of("123"),"jassis@email.com", Role.of("Hauptpächter")));
//	}

	@Test
	void getAll() {
	}

	@Test
	void get() {
	}

	@Test
	void changeRoles() {
	}

	@Test
	void getTenantByUserAccount() {
	}

	@Test
	void tenantHasRole() {
	}

	@Test
	void findEnabled() {
	}

	@Test
	void findDisabled() {
	}

	@Test
	void createTenant(){
		String forename = "Pascall";
		String surname = "Fahrenheit";
		String email = "fahrenheit@web.de";
		String password = email;

		tenantManager.createNewTenant(forename, surname, email, password);
		//assertThat(tenantRepository.existsById(tenantManager.getTenantByUserAccount(userAccountManager.findByUsername("fahrenheit@web.de").get()).getId()));

	}
}
