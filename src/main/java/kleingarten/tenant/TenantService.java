package kleingarten.tenant;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccountManager;

public class TenantService {

	private final TenantManager tenantManager;
	private final UserAccountManager userAccountManager;

	/**
	 * Constructor of the class {@link TenantService}
	 * @param tenantManager manager class of the class {@link Tenant} as {@link TenantManager}
	 * @param userAccountManager repository of userAccounts as {@link UserAccountManager}
	 */
	TenantService(TenantManager tenantManager, UserAccountManager userAccountManager){
		this.tenantManager = tenantManager;
		this.userAccountManager = userAccountManager;
	}

	/** Method to change the password of a userAccount of a {@link Tenant}
	 * @param id identifier of the {@link Tenant}
	 * @param oldPassword old saved password of the userAccount
	 * @param newPassword new password the {@link Tenant} has entered
	 * @param repeatedPassword repeated password to check for spelling mistakes the {@link Tenant} could have made
	 */
	public void changePassword(Long id, String oldPassword, String newPassword, String repeatedPassword){

		if(!oldPassword.equals(tenantManager.get(id).getUserAccount().getPassword())){
			throw new IllegalArgumentException("Old Password is not identical");
		}
		if (!newPassword.equals(repeatedPassword)){
			throw new IllegalArgumentException("New Password and repeated Password are not identical");
		}
		userAccountManager.changePassword(tenantManager.get(id).getUserAccount(), Password.UnencryptedPassword.of(newPassword));
	}
}
