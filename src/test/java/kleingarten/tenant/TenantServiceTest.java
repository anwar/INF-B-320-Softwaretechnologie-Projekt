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
public class TenantServiceTest {
	private final UserAccountManager userAccountManager;
	private final TenantService tenantService;

	private Tenant tenant;

	public TenantServiceTest(@Autowired UserAccountManager userAccountManager, @Autowired TenantService tenantService) {
		this.userAccountManager = userAccountManager;
		this.tenantService = tenantService;
	}

	@BeforeEach
	void SetUp(){
		tenant = new Tenant("Jassi", "Gepackert", "Neben Isa und Francy",
			"908964875734", "13.05.1999", userAccountManager.create("jassi", Password.UnencryptedPassword.of("123"),"jassis@email.com", Role.of("Hauptpächter")));
		tenant.setId(Long.parseLong("1"));
	}

	@Test
	void changePassword() {
		assertThat(tenant.getUserAccount().getPassword().equals("123"));

	}

	@Test
	void changeEmail(){
		//assertThat(tenant.getUserAccount().getEmail().equals("jassis@email.com"));
		tenantService.changeEmail(tenant.getId(), "jassis@email.com", "jasmins@email.com", "jasmins@email.com");
		assertThat(tenant.getUserAccount().getEmail().equals("jasmins@email.com"));
	}
}
