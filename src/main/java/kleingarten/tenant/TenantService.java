package kleingarten.tenant;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccountManager;

public class TenantService {

	private final TenantManager tenantManager;
	private final UserAccountManager userAccountManager;

	TenantService(TenantManager tenantManager, UserAccountManager userAccountManager){
		this.tenantManager = tenantManager;
		this.userAccountManager = userAccountManager;
	}

	public void changePassword(Long id, Password oldPassword, Password newPassword, Password repeatedPassword){
		if(oldPassword.equals(tenantManager.get(id).getUserAccount().getPassword()) && newPassword.equals(repeatedPassword)){
			userAccountManager.changePassword(tenantManager.get(id).getUserAccount(), Password.UnencryptedPassword.of(newPassword.toString()));
		} else if (!oldPassword.equals(tenantManager.get(id).getUserAccount().getPassword()) && newPassword.equals(repeatedPassword)){
			throw new IllegalArgumentException("Old Password is not identical");
		} else if (oldPassword.equals(tenantManager.get(id).getUserAccount().getPassword()) && !newPassword.equals(repeatedPassword)){
			throw new IllegalArgumentException("New Password and repeated Password are not identical");
		}
	}
}
