package kleingarten.tenant;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class TenantManager {

	private final TenantRepository tenants;
	public UserAccountManager userAccounts;

	/**
	 * Constructor of the class {@link TenantManager} used by Spring
	 *
	 * @param tenants      repository of tenants as {@link TenantRepository}
	 * @param userAccounts repository of userAccounts as {@link UserAccountManager}
	 */
	@Autowired
	TenantManager(TenantRepository tenants, UserAccountManager userAccounts) {

		Assert.notNull(tenants, "TenantRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManager must not be null!");

		this.userAccounts = userAccounts;
		this.tenants = tenants;

	}

	/**
	 * Getter to find all registered tenants
	 *
	 * @return Streamable of {@link Tenant}
	 */
	public Streamable<Tenant> getAll() {
		return tenants.findAll();
	}

	/**
	 * Getter to find a tenant by given ID
	 *
	 * @param id Identifier for each {@link Tenant}
	 * @return object of class {@link Tenant}
	 */
	public Tenant get(Long id) {
		return tenants.findById(id).orElse(null);
	}

	/**
	 * Getter to find a tenant by its userAccount
	 *
	 * @param userAccount userAccount of tenant to find
	 * @return object of class {@link Tenant}
	 */
	public Tenant getTenantByUserAccount(UserAccount userAccount) {
		if (tenants.findByUserAccount(userAccount).isEmpty()) {
			throw new IllegalArgumentException("Tenant not found");
		} else {
			return tenants.findByUserAccount(userAccount).get();
		}
	}

	/**
	 * Method to determine whether or not a {@link Tenant} has a specific {@link Role}
	 *
	 * @param tenant to test if {@link Tenant} has the {@link Role}
	 * @param role   to test
	 * @return boolean true if {@link Role} is found, boolean false if not
	 */
	public boolean tenantHasRole(Tenant tenant, Role role) {
		return tenant.getUserAccount().hasRole(role);
	}

	/**
	 * Method to find enabled userAccounts of tenants
	 *
	 * @return Streamable of class {@link Tenant} of all {@link Tenant}s with enabled {@link UserAccount}s
	 */
	public Streamable<Tenant> findEnabled() {
		return getAll().filter(c -> c.getUserAccount().isEnabled());
	}

	/**
	 * Method to find disabled userAccounts of tenants
	 *
	 * @return Streamable of class {@link Tenant} of all {@link Tenant}s with disabled {@link UserAccount}s
	 */
	public Streamable<Tenant> findDisabled() {
		return getAll().filter(c -> !c.getUserAccount().isEnabled());
	}

	/**
	 * Method to create a new {@link Tenant} of an accepted {@link kleingarten.application.Application}
	 *
	 * @param forename of the {@link Tenant} as {@link String}
	 * @param surname  of the {@link Tenant} as {@link String}
	 * @param email    of the {@link Tenant} as {@link String}
	 * @param password of the {@link Tenant} as {@link String}
	 */
	public void createNewTenant(String forename, String surname, String email, String password) {
		Tenant tenant = new Tenant(forename, surname, "", "", "",
				userAccounts.create(email, Password.UnencryptedPassword.of(password), email));
		tenant.addRole(Role.of("Hauptpächter"));
		tenants.save(tenant);
	}

	/**
	 * Method to create a new Person who can become an additional {@link Tenant}
	 *
	 * @param forename    of the {@link Tenant} as {@link String}
	 * @param surname     of the {@link Tenant} as {@link String}
	 * @param address     of the {@link Tenant} as {@link String}
	 * @param phonenumber of the {@link Tenant} as {@link String}
	 * @param birthdate   of the {@link Tenant} as {@link String}
	 * @param email       of the {@link Tenant} as {@link String}
	 */
	public void createNewPerson(String forename, String surname, String address, String phonenumber,
								String birthdate, String email) {
		Tenant tenant = new Tenant(forename, surname, address, phonenumber, birthdate, userAccounts.create(email,
				Password.UnencryptedPassword.of(phonenumber), email));
		tenants.save(tenant);
	}


	/**
	 * Method to find {@link Tenant}s by their {@link Role}
	 *
	 * @param role to find the {@link Tenant}s by
	 * @return {@link List} of {@link Tenant}s
	 */
	public List<Tenant> findByRole(Role role) {
		return tenants.findAll().filter(t -> t.getUserAccount().hasRole(role)).toList();
	}


	/**
	 * Generates random Password for a new {@link Tenant}
	 *
	 * @param n lenght of the password
	 * @return random password as {@link String}
	 * Source code found on https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
	 * authored by Rajput-Ji https://auth.geeksforgeeks.org/user/Rajput-Ji/articles
	 */
	public String generatedPassword(int n) {
		String generatedPassword = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "0123456789"
				+ "abcdefghijklmnopqrstuvwxyz";

		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			int index
					= (int) (generatedPassword.length()
					* Math.random());

			sb.append(generatedPassword
					.charAt(index));
		}

		return sb.toString();
	}
}
