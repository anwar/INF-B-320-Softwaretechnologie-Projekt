package kleingarten.tenant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
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


	/**
	 * Set up for a mock {@link Tenant} for each testing
	 */
	@BeforeEach
	void SetUp(){
		tenant = new Tenant("Jassi", "Gepackert", "Neben Isa und Francy",
			"908964875734", "13.05.1999", userAccountManager.create("jassi", Password.UnencryptedPassword.of("123"),"jassis@email.com", Role.of("Hauptpächter")));
	}

	/**
	 * Test to determine if the initialization of the {@link Role} for the {@link Tenant} worked
	 */
	@Test
	void initialRoleTest(){
		assertThat(tenant.getUserAccount().getRoles().equals("Hauptpächchter"));
	}

	/**
	 * Test to determine if the initialization of the {@link UserAccount} for the {@link Tenant} worked
	 */
	@Test
	void initialUserAccountTest(){
		assertThat(tenant.getUserAccount().getUsername().equals("jassi"));
		assertThat(tenant.getUserAccount().getEmail().equals("jassis@email.com"));
		assertThat(tenant.getUserAccount().getPassword().equals("123"));
	}


	/**
	 * Test for the Getter of the address
	 */
	@Test
	void getAddress() {
		assertThat(tenant.getAddress().equals("Neben Isa und Francy"));
	}

	/**
	 * Test for the Getter of the forename
	 */
	@Test
	void getForename() {
		assertThat(tenant.getForename().equals("Jassi"));
	}

	/**
	 * Test for the Getter of the surname
	 */
	@Test
	void getSurname() {
		assertThat(tenant.getSurname().equals("Gepackert"));
	}

	/**
	 * Test for the Getter of the phone number
	 */
	@Test
	void getPhonenumber() {
		assertThat(tenant.getPhonenumber().equals("908964875734"));
	}

	/**
	 * Test for the Getter of the birth date
	 */
	@Test
	void getBirthdate() {
		assertThat(tenant.getBirthdate().equals("13.05.1999"));
	}

	/**
	 * Test for the Getter of the {@link Role}s
	 */
	@Test
	void getRoles() {
		assertThat(tenant.getRoles().equals("Hauptpächter"));
	}

	/**
	 * Test for the Getter of a {@link Role}
	 */
	@Test
	void getRole() {
		assertThat(tenant.getRole().equals("Hauptpächter"));
	}

	/**
	 * Test for the Getter of the email
	 */
	@Test
	void getEmail() {
		assertThat(tenant.getEmail().equals("jassis@email.com"));
	}

	/**
	 * Test for the Setter of the forename
	 */
	@Test
	void setForename() {
		tenant.setForename("Hans");
		assertThat(tenant.getForename().equals("Hans"));
	}

	/**
	 * Test for the Setter of an illegal forename to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalForename(){
		String forename = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setForename(forename));
	}

	/**
	 * Test for the Setter of the surname
	 */
	@Test
	void setSurname() {
		tenant.setSurname("Wurst");
		assertThat(tenant.getSurname().equals("Wurst"));
	}

	/**
	 * Test for the Setter of an illegal surname to throw an I{@link IllegalArgumentException}
	 */
	@Test
	void illegalSurname(){
		String surname = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setSurname(surname));
	}

	/**
	 * Test for the Setter of the address
	 */
	@Test
	void setAddress() {
		tenant.setAddress("Drei Blöcke daneben");
		assertThat(tenant.getAddress().equals("Drei Blöcke daneben"));
	}

	/**
	 * Test for the Setter of an illegal address to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalAddress(){
		String address = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setAddress(address));
	}

	/**
	 * Test for the Setter of the email
	 */
	@Test
	void setEmail() {
		tenant.setEmail("Jasmins@email.com");
		assertThat(tenant.getUserAccount().getEmail().equals("Jasmins@email.com"));
	}

	/**
	 * Test for the Setter of an illegal email to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalEmail(){
		String email = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setEmail(email));
	}

	/**
	 * Test for the Setter of the phone number
	 */
	@Test
	void setPhonenumber() {
		tenant.setPhonenumber("89765434568790");
		assertThat(tenant.getPhonenumber().equals("89765434568790"));
	}
	/**
	 * Test for the Setter of an illegal phone number to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalPhonenumber(){
		String phonenumber = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setAddress(phonenumber));
	}

	/**
	 * Test for the Setter of the birth date
	 */
	@Test
	void setBirthdate() {
		tenant.setBirthdate("16.09.678");
		assertThat(tenant.getBirthdate().equals("16.09.678"));
	}

	/**
	 * Test for the Setter of an illegal birth date to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalBirthdate(){
		String birthdate = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setBirthdate(birthdate));
	}

	/**
	 * Test for adding a {@link Role}
	 */
	@Test
	void addRole() {
		tenant.addRole(Role.of("Vostandsvorsitzender"));
		assertThat(tenant.getRoles().equals("Hauptpächchter, Vorstandsvorsitzender"));
	}

	/**
	 * Test for removing a {@link Role}
	 */
	@Test
	void removeRole() {
		tenant.removeRole(Role.of("Hauptpächter"));
		assertThat(tenant.getRoles().equals(""));
	}
}
