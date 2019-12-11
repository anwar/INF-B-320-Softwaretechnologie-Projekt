 package kleingarten.complains;


import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class ComplainsTest {

	private Complains complains;
	private final UserAccountManager userAccountManager;

	private TenantManager tenantManager;

	public ComplainsTest(@Autowired UserAccountManager userAccountManager, @Autowired TenantManager tenantManager){
		this.userAccountManager = userAccountManager;
		this.tenantManager = tenantManager;
	}

	@BeforeEach
	void SetUp(){

		complains = new Complains(tenantManager.getAll().toList().get(0).getId(), tenantManager.getAll().toList().get(1).getId(),
			ComplainsState.PENDING, "Der ist zu laut");
	}

	@Test
	void getAuthor() {
		assertThat(complains.getAuthor() == (tenantManager.getAll().toList().get(0).getId()));
	}

	@Test
	void getSubject() {
		assertThat(complains.getSubject() == tenantManager.getAll().toList().get(1).getId());
	}


	@Test
	void getState() {
		assertThat(complains.getState().equals(ComplainsState.PENDING));
	}

	@Test
	void getDescription() {
		assertThat(complains.getDescription().equals("Der ist zu laut"));
	}

	@Test
	void setAuthor() {
		complains.setAuthor(tenantManager.getAll().toList().get(2).getId());
		assertThat(complains.getAuthor() == (tenantManager.getAll().toList().get(2).getId()));
	}

	@Test
	void setIllegalAuthor(){
		Long authorId = Long.valueOf(0);
		assertThrows(IllegalArgumentException.class, () -> complains.setAuthor(authorId));
	}

	@Test
	void setSubject() {
		complains.setSubject(tenantManager.getAll().toList().get(0).getId());
		assertThat(complains.getSubject() == (tenantManager.getAll().toList().get(0).getId()));
	}

	@Test
	void setIllegalSubject(){
		Long subjectId = Long.valueOf(0);
		assertThrows(IllegalArgumentException.class, () -> complains.setSubject(subjectId));
	}

	@Test
	void setState() {
		complains.setState(ComplainsState.FINISHED);
		assertThat(complains.getState().equals(ComplainsState.FINISHED));
	}

	@Test
	void setIllegalState(){
		ComplainsState state = null;
		assertThrows(IllegalArgumentException.class, () -> complains.setState(state));
	}

	@Test
	void setDescription() {
		complains.setDescription("Der ist doof.");
		assertThat(complains.getDescription().equals("Der ist doof."));
	}

	@Test
	void setIllegalDescription(){
		String description = null;
		assertThrows(IllegalArgumentException.class, () -> complains.setDescription(description));
	}
}
