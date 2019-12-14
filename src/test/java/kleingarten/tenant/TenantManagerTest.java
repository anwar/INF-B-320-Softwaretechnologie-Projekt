package kleingarten.tenant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


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

	@Test
	void getTenantByUserAccount() {
		assertThat(tenantManager.getTenantByUserAccount(userAccountManager.findByUsername("peter.klaus").get()).getForename().equals("Peter"));
	}

	@Test
	void tenantHasRole() {
		assertTrue(tenantManager.get(Long.valueOf(10)).hasRole("Hauptpächter"));
	}

	@Test
	void findEnabled() {
	}

	@Test
	void findDisabled() {
	}

	@Test
	void createNewTenant(){
		assertThat(tenantRepository.existsById(tenantManager.getTenantByUserAccount(userAccountManager.findByUsername("fahrenheit@web.de").get()).getId()));
	}

	@Test
	void findByRole(){
		Role testRole = Role.of("Pupskönig");
		tenantManager.findByRole(testRole);
		assertTrue(tenantManager.findByRole(testRole).isEmpty());
	}

	@Test
	void createNewPerson() {
		assertThat(tenantRepository.existsById(tenantManager.getTenantByUserAccount(userAccountManager.findByUsername("fred.feuerstain@steinmail.com").get()).getId()));
	}
}
