package kleingarten.tenant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
	void SetUp() {
		tenant = new Tenant("Jassi", "Gepackert", "Neben Isa und Francy",
				"908964875734", "13.05.1999", userAccountManager.create("jassi", Password.UnencryptedPassword.of("123"), "jassis@email.com", Role.of("Hauptpächter")));
	}

	/**
	 * Test to determine if the initialization of the {@link Role} for the {@link Tenant} worked
	 */
	@Test
	void initialRoleTest() {
		assertThat(tenant.getUserAccount().getRoles().equals("Hauptpächchter"));
	}

	/**
	 * Test to determine if the initialization of the {@link UserAccount} for the {@link Tenant} worked
	 */
	@Test
	void initialUserAccountTest() {
		assertEquals("jassi", tenant.getUserAccount().getUsername());
		assertEquals("jassis@email.com", tenant.getUserAccount().getEmail());

	}


	/**
	 * Test for the Getter of the address
	 */
	@Test
	void getAddress() {
		assertEquals("Neben Isa und Francy",tenant.getAddress());
	}

	/**
	 * Test for the Getter of the forename
	 */
	@Test
	void getForename() {
		assertEquals("Jassi", tenant.getForename());
	}

	/**
	 * Test for the Getter of the surname
	 */
	@Test
	void getSurname() {
		 assertEquals("Gepackert",tenant.getSurname());
	}

	/**
	 * Test for the Getter of the phone number
	 */
	@Test
	void getPhonenumber() {
		assertEquals("908964875734", tenant.getPhonenumber());
	}

	/**
	 * Test for the Getter of the birth date
	 */
	@Test
	void getBirthdate() {
		assertEquals("13.05.1999", tenant.getBirthdate());
	}

	/**
	 * Test for the Getter of the {@link Role}s
	 */
	@Test
	void getRoles() {
		assertEquals("Hauptpächter", tenant.getRoles());
	}

	/**
	 * Test for the Getter of a {@link Role}
	 */
	@Test
	void getRole() {
		assertEquals(Role.of("Hauptpächter"), tenant.getRole());
	}

	/**
	 * Test for the Getter of the email
	 */
	@Test
	void getEmail() {
		assertEquals("jassis@email.com", tenant.getEmail());
	}

	/**
	 * Test for the Setter of the forename
	 */
	@Test
	void setForename() {
		tenant.setForename("Hans");
		assertEquals("Hans", tenant.getForename());
	}

	/**
	 * Test for the Setter of an illegal forename to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalForename() {
		String forename = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setForename(forename));
	}

	/**
	 * Test for the Setter of the surname
	 */
	@Test
	void setSurname() {
		tenant.setSurname("Wurst");
		assertEquals("Wurst", tenant.getSurname());
	}

	/**
	 * Test for the Setter of an illegal surname to throw an I{@link IllegalArgumentException}
	 */
	@Test
	void illegalSurname() {
		String surname = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setSurname(surname));
	}

	/**
	 * Test for the Setter of the address
	 */
	@Test
	void setAddress() {
		tenant.setAddress("Drei Blöcke daneben");
		assertEquals("Drei Blöcke daneben", tenant.getAddress());
	}

	/**
	 * Test for the Setter of an illegal address to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalAddress() {
		String address = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setAddress(address));
	}

	/**
	 * Test for the Setter of the email
	 */
	@Test
	void setEmail() {
		tenant.setEmail("Jasmins@email.com");
		assertEquals("Jasmins@email.com", tenant.getUserAccount().getEmail());
	}

	/**
	 * Test for the Setter of an illegal email to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalEmail() {
		String email = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setEmail(email));
	}

	/**
	 * Test for the Setter of the phone number
	 */
	@Test
	void setPhonenumber() {
		tenant.setPhonenumber("89765434568790");
		assertEquals("89765434568790", tenant.getPhonenumber());
	}

	/**
	 * Test for the Setter of an illegal phone number to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalPhonenumber() {
		String phonenumber = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setAddress(phonenumber));
	}

	/**
	 * Test for the Setter of the birth date
	 */
	@Test
	void setBirthdate() {
		tenant.setBirthdate("16.09.678");
		assertEquals("16.09.678", tenant.getBirthdate());
	}

	/**
	 * Test for the Setter of an illegal birth date to throw an {@link IllegalArgumentException}
	 */
	@Test
	void illegalBirthdate() {
		String birthdate = null;
		assertThrows(IllegalArgumentException.class, () -> tenant.setBirthdate(birthdate));
	}

	/**
	 * Test for adding a {@link Role}
	 */
	@Test
	void addRole() {
		tenant.addRole(Role.of("Obmann"));
		assertEquals("Hauptpächter, Obmann", tenant.getRoles());
	}

	/**
	 * Test for removing a {@link Role}
	 */
	@Test
	void removeRole() {
		tenant.removeRole(Role.of("Hauptpächter"));
		assertEquals("", tenant.getRoles());
	}

	@Test
	void deleteRoles() {
		tenant.deleteRoles();
		assertEquals("", tenant.getRoles());
	}

	@Test
	void hasRole() {
		assertTrue(tenant.hasRole("Hauptpächter"));
	}
}
