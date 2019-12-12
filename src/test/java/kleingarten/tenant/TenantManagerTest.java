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
	void createNewTenant(){
		assertThat(tenantRepository.existsById(tenantManager.getTenantByUserAccount(userAccountManager.findByUsername("fahrenheit@web.de").get()).getId()));
	}

	@Test
	void findByRole(){
		Role testRole = Role.of("Pupskönig");
		tenantManager.findByRole(testRole);
		assertTrue(tenantManager.findByRole(testRole).isEmpty());
		System.out.println(tenantManager.findByRole(Role.of("Hauptpächter")));
		System.out.println(tenantManager.findByRole(Role.of("Hauptpächter")).size());
		//assertThat(tenantManager.findByRole(Role.of("Hauptpächter")).size() == 4);
		//assertTrue(tenantManager.findByRole(Role.of("Vorstandsvorsitzender")).size() == 0);
		//assertTrue(tenantManager.findByRole(Role.of("Vorstandsvorsitzender")).size() == 1);
	}
}
