package kleingarten.complaint;


import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useDefaultDateFormatsOnly;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class ComplaintManagerTests {

	private ComplaintManager complaintManager;
	private TenantManager tenantManager;
	private UserAccountManager userAccountManager;

	public ComplaintManagerTests(@Autowired ComplaintManager complaintManager, @Autowired TenantManager tenantManager,
								 @Autowired UserAccountManager userAccountManager){
		this.complaintManager = complaintManager;
		this.tenantManager = tenantManager;
		this.userAccountManager = userAccountManager;
	}
	
	@Test
	void getForObmann(){
		assertFalse(complaintManager.getForObmann(tenantManager.findByRole(Role.of("Obmann")).get(0)).isEmpty());

	}

	@Test
	void getForComplainant() {
		assertFalse(complaintManager.getForComplainant(tenantManager.getTenantByUserAccount(userAccountManager.findByUsername("neptun").get())).isEmpty());
	}
}
