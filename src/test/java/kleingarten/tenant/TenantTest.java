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
		assertThat(tenant.getUserAccount().getEmail().equals("jassis@email.com"));
		assertThat(tenant.getUserAccount().getPassword().equals("123"));
	}

	@Test
	void getAddress() {
		assertThat(tenant.getAddress().equals("Neben Isa und Francy"));
	}

	@Test
	void getForename() {
		assertThat(tenant.getForename().equals("Jassi"));
	}

	@Test
	void getSurname() {
		assertThat(tenant.getSurname().equals("Gepackert"));
	}

	@Test
	void getPhonenumber() {
		assertThat(tenant.getPhonenumber().equals("908964875734"));
	}

	@Test
	void getBirthdate() {
		assertThat(tenant.getBirthdate().equals("13.05.1999"));
	}

	@Test
	void getId() {
		//assertThat();
	}


	@Test
	void getRoles() {
	}

	@Test
	void getRole() {
	}

	@Test
	void getEmail() {
		assertThat(tenant.getEmail().equals("jassis@email.com"));
	}

	@Test
	void setForename() {
	}

	@Test
	void setSurname() {
	}

	@Test
	void setAddress() {
	}

	@Test
	void setEmail() {
	}

	@Test
	void setPhonenumber() {
	}

	@Test
	void setBirthdate() {
	}

	@Test
	void addRole() {
	}

	@Test
	void removeRole() {
	}

	@Test
	void setUserAccount() {
	}

}
