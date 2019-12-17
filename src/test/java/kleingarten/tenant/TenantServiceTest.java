package kleingarten.tenant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class TenantServiceTest {
	private final UserAccountManager userAccountManager;
	private final TenantService tenantService;
	private final TenantRepository tenantRepository;
	private final TenantManager tenantManager;

	private Tenant tenant;

	//TenantService(TenantManager tenantManager, UserAccountManager userAccountManager, TenantRepository tenants, AuthenticationManager authenticationManager)
	public TenantServiceTest(@Autowired UserAccountManager userAccountManager, @Autowired TenantRepository tenantRepository, @Autowired AuthenticationManager authenticationManager) {
		this.userAccountManager = userAccountManager;
		this.tenantRepository = tenantRepository;
		this.tenantManager = new TenantManager(tenantRepository, userAccountManager);
		this.tenantService = new TenantService(tenantManager, userAccountManager, tenantRepository, authenticationManager);
	}

	@BeforeEach
	void SetUp() {

	}

	@Test
	void changePassword() {
		tenantService.changePassword(tenantRepository.findAll().toList().get(0).getUserAccount(), "123", "1234", "1234");
		assertThat(tenantRepository.findAll().toList().get(0).getUserAccount().getPassword().equals("1234"));
	}

	@Test
	void illegalOldPassword() {
		assertThrows(IllegalArgumentException.class, () -> tenantService.changePassword(tenantRepository.findAll().toList().get(0).getUserAccount(), "1234", "1234", "1234"));
	}

	@Test
	void IllegalRepeatedPassword() {
		assertThrows(IllegalArgumentException.class, () -> tenantService.changePassword(tenantRepository.findAll().toList().get(0).getUserAccount(), "123", "1234", "12345"));
	}

	@Test
	void changeEmail() {
		tenantService.changeEmail(tenantRepository.findAll().toList().get(0).getId(), tenantRepository.findAll().toList().get(0).getEmail(), "jasmins@email.com", "jasmins@email.com");
		assertThat(tenantRepository.findAll().toList().get(0).getEmail().equals("jasmins@email.com"));
	}

	@Test
	void IllegalOldEmail() {
		assertThrows(IllegalArgumentException.class, () -> tenantService.changeEmail(tenantRepository.findAll().toList().get(0).getId(), "1234", "jasmins@email.com", "jasmins@email.com"));
	}

	@Test
	void IllegalRepeatedEmail() {
		assertThrows(IllegalArgumentException.class, () -> tenantService.changeEmail(tenantRepository.findAll().toList().get(0).getId(), tenantRepository.findAll().toList().get(0).getEmail(), "jasmins@email.com", "jasmins1@email.com"));
	}

	@Test
	void makePreTenant() {
		tenantService.makePreTenant(tenantManager.getAll().toList().get(0).getId());
		assertThat(tenantManager.getAll().toList().get(0).getForename().equals(""));
	}
}
