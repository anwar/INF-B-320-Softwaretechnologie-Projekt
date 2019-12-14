package kleingarten.tenant;

import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;

@Component
public class TenantService {

	private final TenantManager tenantManager;
	private final UserAccountManager userAccountManager;
	private final TenantRepository tenants;
	private final AuthenticationManager authenticationManager;

	/**
	 * Constructor of the class {@link TenantService}
	 * @param tenantManager manager class of the class {@link Tenant} as {@link TenantManager}
	 * @param userAccountManager repository of userAccounts as {@link UserAccountManager}
	 */
	TenantService(TenantManager tenantManager, UserAccountManager userAccountManager, TenantRepository tenants, AuthenticationManager authenticationManager){
		this.tenantManager = tenantManager;
		this.userAccountManager = userAccountManager;
		this.tenants = tenants;
		this.authenticationManager = authenticationManager;
	}

	/** Method to change the password of a userAccount of a {@link Tenant}
	 * @param userAccount {@link UserAccount} of the {@link Tenant}
	 * @param oldPassword old saved password of the userAccount
	 * @param newPassword new password the {@link Tenant} has entered
	 * @param repeatedPassword repeated password to check for spelling mistakes the {@link Tenant} could have made
	 */
	void changePassword(UserAccount userAccount, String oldPassword, String newPassword, String repeatedPassword){

		if(!authenticationManager.matches(Password.UnencryptedPassword.of(oldPassword), userAccount.getPassword())){

			throw new IllegalArgumentException("Old Password is not identical");
		}
		if (!newPassword.equals(repeatedPassword)){
			throw new IllegalArgumentException("New Password and repeated Password are not identical");
		}
		userAccountManager.changePassword(tenantManager.getTenantByUserAccount(userAccount).getUserAccount(), Password.UnencryptedPassword.of(newPassword));
	}

	/**
	 * Method to change the Email of a {@link Tenant}
	 * @param id of the {@link Tenant}
	 * @param oldEmail of the {@link Tenant} as {@link String}
	 * @param newEmail of the {@link Tenant} as {@link String}
	 * @param repeatedEmail to verify and check if the {@link Tenant} had a typo in their new Email
	 */
	void changeEmail(Long id, String oldEmail, String newEmail, String repeatedEmail){
		if(!oldEmail.equals(tenantManager.get(id).getUserAccount().getEmail())){
			throw new IllegalArgumentException("Old Email not identical");
		}
		if(!newEmail.equals(repeatedEmail)){
			throw new IllegalArgumentException("New email and repeated email are not identical");
		}
		tenantManager.get(id).getUserAccount().setEmail(newEmail);
		tenants.save(tenantManager.get(id));
	}

	/**
	 * Method to make an existing {@link Tenant} a pre tenant
	 * @param id Id of the {@link Tenant} we want to make a pre {@link Tenant}
	 */
	void makePreTenant(Long id){
		tenantManager.get(id).getUserAccount().setEnabled(false);
		tenantManager.get(id).setBirthdate("");
		tenantManager.get(id).setPhonenumber("");
		tenantManager.get(id).setAddress("");
		tenantManager.get(id).setForename("");
		tenantManager.get(id).setSurname("");
		tenantManager.get(id).deleteRoles();
	}
}
