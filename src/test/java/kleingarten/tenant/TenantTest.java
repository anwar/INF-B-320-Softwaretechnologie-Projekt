package kleingarten.tenant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class TenantTest {

	private final UserAccountManager userAccountManager;

	private Tenant tenant;

	public TenantTest(@Autowired UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}



	@BeforeEach
	void SetUp(){
		tenant = new Tenant("Jassi", "Gepackert", "Neben Isa und Francy",
			"908964875734", "13.05.1999", userAccountManager.create("jassi", Password.UnencryptedPassword.of("123"),"jassis@email.com", Role.of("Hauptpächter")));
	}

	@Test
	void initialRoleTest(){
		assertThat(tenant.getUserAccount().getRoles().equals("Hauptpächchter"));
	}

	@Test
	void initialUserAccountTest(){
		assertThat(tenant.getUserAccount().getUsername().equals("jassi"));
	}

	@Test
	void initialPasswordTest(){
		assertThat(tenant.getUserAccount().getPassword().equals("123"));
	}

	@Test
	void initializeTenant(){
		assertThat(tenant.getForename().equals("Jasmin"));
		assertThat(tenant.getSurname().equals("Gepackert"));

	}


}
