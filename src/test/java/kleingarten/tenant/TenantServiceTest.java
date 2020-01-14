package kleingarten.tenant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TenantServiceTest {
	private final UserAccountManager userAccountManager;
	private final TenantService tenantService;
	private final TenantRepository tenantRepository;
	private final TenantManager tenantManager;
	private final AuthenticationManager authenticationManager;

	private Tenant tenant;

	//TenantService(TenantManager tenantManager, UserAccountManager userAccountManager, TenantRepository tenants, AuthenticationManager authenticationManager)
	public TenantServiceTest(@Autowired UserAccountManager userAccountManager, @Autowired TenantRepository tenantRepository,
							 @Autowired AuthenticationManager authenticationManager) {
		this.userAccountManager = userAccountManager;
		this.tenantRepository = tenantRepository;
		this.tenantManager = new TenantManager(tenantRepository, userAccountManager);
		this.tenantService = new TenantService(tenantManager, userAccountManager, tenantRepository, authenticationManager);
		this.authenticationManager = authenticationManager;

	}

	@Test
	void changePassword() {
		tenantService.changePassword(tenantRepository.findAll().toList().get(0).getUserAccount(),
				"123", "1234", "1234");
		assertTrue(authenticationManager.matches(Password.UnencryptedPassword.of("1234"),
				tenantRepository.findAll().toList().get(0).getUserAccount().getPassword()));
	}

	@Test
	void illegalOldPassword() {
		assertThrows(IllegalArgumentException.class, () ->
				tenantService.changePassword(tenantRepository.findAll().toList().get(0).getUserAccount(),
						"1234", "1234", "1234"));
	}

	@Test
	void IllegalRepeatedPassword() {
		assertThrows(IllegalArgumentException.class, () ->
				tenantService.changePassword(tenantRepository.findAll().toList().get(0).getUserAccount(),
						"123", "1234", "12345"));
	}

	@Test
	void changeEmail() {
		tenantService.changeEmail(tenantRepository.findAll().toList().get(0).getId(), tenantRepository.findAll().toList().get(0).getEmail(),
				"jasmins@email.com", "jasmins@email.com");
		assertEquals("jasmins@email.com", tenantRepository.findAll().toList().get(0).getEmail());
	}

	@Test
	void IllegalOldEmail() {
		assertThrows(IllegalArgumentException.class, () ->
				tenantService.changeEmail(tenantRepository.findAll().toList().get(0).getId(),
						"1234", "jasmins@email.com", "jasmins@email.com"));
	}

	@Test
	void IllegalRepeatedEmail() {
		assertThrows(IllegalArgumentException.class, () ->
				tenantService.changeEmail(tenantRepository.findAll().toList().get(0).getId(),
						tenantRepository.findAll().toList().get(0).getEmail(),
						"jasmins@email.com", "jasmins1@email.com"));
	}

	@Test
	void makePreTenant() {
		tenantService.makePreTenant(tenantManager.getAll().toList().get(0).getId());
		assertEquals("", tenantManager.getAll().toList().get(0).getForename());
	}
}
