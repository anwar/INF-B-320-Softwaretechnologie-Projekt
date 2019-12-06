package kleingarten.tenant;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class TenantServiceTest {
	private final UserAccountManager userAccountManager;

	private Tenant tenant;

	public TenantServiceTest(@Autowired UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	@BeforeEach
	void SetUp(){
		tenant = new Tenant("Jassi", "Gepackert", "Neben Isa und Francy",
			"908964875734", "13.05.1999", userAccountManager.create("jassi", Password.UnencryptedPassword.of("123"),"jassis@email.com", Role.of("Hauptpächter")));
	}

	@Test
	void changePassword() {
	}
}
