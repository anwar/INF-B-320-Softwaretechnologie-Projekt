package kleingarten.tenant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TenantTest {

	private final UserAccountManager userAccountManager;

	private Tenant tenant;

	public TenantTest(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	@BeforeEach
	public void SetUp(){
		tenant = new Tenant("Jassi", "Gepackert", "Neben Isa und Francy",
			"908964875734", "13.05.1999", userAccountManager.create("jassi", Password.UnencryptedPassword.of("123"),"jassis@email.com", Role.of("Hauptpächter")));
	}

	@Test
	public void initialRoleTest(){
		assertThat(tenant.getRole().equals("Hauptpächter"));
	}

	@Test
	public void initialUserAccountTest(){
		assertThat(tenant.getUserAccount().getUsername().equals("jassi"));
	}

	@Test
	public void initialPasswordTest(){
		assertThat(tenant.getUserAccount().getPassword().equals("123"));
	}

}
