package kleingarten.tenant;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class TenantManager {

	private final TenantRepository tenants;
	public UserAccountManager userAccounts;

	/**
	 * Constructor of the class {@link TenantManager} used by Spring
	 * @param tenants repository of tenants as {@link TenantRepository}
	 * @param userAccounts repository of userAccounts as {@link UserAccountManager}
	 */
	@Autowired
	TenantManager(TenantRepository tenants, UserAccountManager userAccounts){

		Assert.notNull(tenants, "TenantRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManager must not be null!");

		this.userAccounts = userAccounts;
		this.tenants = tenants;

	}

	/**
	 * Getter to find all registered tenants
	 * @return Streamable of {@link Tenant}
	 */
	public Streamable<Tenant> getAll(){
		return tenants.findAll();
	}

	/**
	 * Getter to find a tenant by given ID
	 * @param id Identifier for each {@link Tenant}
	 * @return object of class {@link Tenant}
	 */
	public Tenant get(Long id){
		return tenants.findById(id).orElse(null);
	}

	/**
	 * Method to change the roles of a {@link Tenant}
	 * @param role old role to be changed
	 * @param newRole new role to be added, removes role if null
	 * @param tenant tenant who gets its roles changed
	 * @return
	 */
	public void changeRoles(Role role, Role newRole,Tenant tenant){
		tenant.getUserAccount().getRoles().stream().filter(c -> c.equals(role)).map(n -> tenant.getUserAccount().remove(n));
		if(newRole != null) {
			tenant.getUserAccount().add(newRole);
		}
	}

	/**
	 * Getter to find a tenant by its userAccount
	 * @param userAccount userAccount of tenant to find
	 * @return object of class {@link Tenant}
	 */
	public Tenant getTenantByUserAccount(UserAccount userAccount){
		if (tenants.findByUserAccount(userAccount).isEmpty()){
			throw new IllegalArgumentException("Tenant not found");
		} else {
			return tenants.findByUserAccount(userAccount).get();
		}
	}

	/**
	 * Method to determine whether or not a tenant has a specific role
	 * @param tenant to test if its has the role
	 * @param role to test
	 * @return boolean true if role is found, boolean false if not
	 */
	public boolean tenantHasRole(Tenant tenant, Role role){
		return tenant.getUserAccount().hasRole(role);
	}

	/**
	 * Method to find enabled userAccounts of tenants
	 * @return Streamable of class {@link Tenant} of all tenants with enabled userAccounts
	 */
	public Streamable<Tenant> findEnabled(){
		return tenants.findAll().filter(c -> c.getUserAccount().isEnabled());
	}

	/**
	 * Method to find disabled userAccounts of tenants
	 * @return Streamable of class {@link Tenant} of all tenants with disabled userAccounts
	 */
	public  Streamable<Tenant> findDisabled(){
		return  tenants.findAll().filter(c -> !c.getUserAccount().isEnabled());
	}

	/**
	 * @param forename
	 * @param surname
	 * @param email
	 * @param password
	 */
	public void createNewTenant(String forename, String surname, String email, String password){
		Tenant tenant = new Tenant(forename, surname, "", "", "", userAccounts.create(email, Password.UnencryptedPassword.of(password), email));
		tenant.addRole(Role.of("Hauptpächter"));
		tenants.save(tenant);
	}

	/**
	 * Method to create a new Person who can become an additional tenant
	 * @param forename
	 * @param surname
	 * @param address
	 * @param phonenumber
	 * @param birthdate
	 * @param email
	 */
	public void createNewPerson(String forename, String surname, String address, String phonenumber, String birthdate, String email){
		Tenant tenant = new Tenant(forename, surname, address, phonenumber, birthdate, userAccounts.create(email, Password.UnencryptedPassword.of(phonenumber), email));
		tenants.save(tenant);
	}


	public List<Tenant> findByRole(Role role){
		return tenants.findAll().filter(t -> t.getUserAccount().hasRole(role)).toList();
	}
}
