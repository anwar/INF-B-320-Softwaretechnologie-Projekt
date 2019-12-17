package kleingarten.tenant;


import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class TenantRoleTest {
	private final UserAccountManager userAccountManager;


	public TenantRoleTest(@Autowired UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	@Test
	void testToString() {
	}

	@Test
	void compareTo() {
	}

	@Test
	void indexOf() {
	}

	@Test
	void testToString1() {
	}

	@Test
	void getRoleList() {
	}

	@Test
	void getUniqueRoleList() {
	}

}
