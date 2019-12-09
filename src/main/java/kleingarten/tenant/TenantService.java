package kleingarten.tenant;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;

@Component
public class TenantService {

	private final TenantManager tenantManager;
	private final UserAccountManager userAccountManager;
	private final TenantRepository tenants;

	/**
	 * Constructor of the class {@link TenantService}
	 * @param tenantManager manager class of the class {@link Tenant} as {@link TenantManager}
	 * @param userAccountManager repository of userAccounts as {@link UserAccountManager}
	 */
	TenantService(TenantManager tenantManager, UserAccountManager userAccountManager, TenantRepository tenants){
		this.tenantManager = tenantManager;
		this.userAccountManager = userAccountManager;
		this.tenants = tenants;
	}

	/** Method to change the password of a userAccount of a {@link Tenant}
	 * @param id identifier of the {@link Tenant}
	 * @param oldPassword old saved password of the userAccount
	 * @param newPassword new password the {@link Tenant} has entered
	 * @param repeatedPassword repeated password to check for spelling mistakes the {@link Tenant} could have made
	 */
	void changePassword(UserAccount userAccount, Password oldPassword, Password newPassword, Password repeatedPassword){

		if(!Password.UnencryptedPassword.of(oldPassword.toString()).equals(Password.UnencryptedPassword.of(userAccount.getPassword().toString()))){
			throw new IllegalArgumentException("Old Password is not identical");
		}
		if (!newPassword.equals(repeatedPassword)){
			throw new IllegalArgumentException("New Password and repeated Password are not identical");
		}
		userAccountManager.changePassword(tenantManager.getTenantByUserAccount(userAccount).getUserAccount(), Password.UnencryptedPassword.of(newPassword.toString()));
	}

	/**
	 * @param id
	 * @param oldEmail
	 * @param newEmail
	 * @param repeatedEmail
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
}
