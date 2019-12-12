package kleingarten.complaint;


import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ComplaintTests {

	private final UserAccountManager userAccountManager;
	private Complaint complaints;
	private TenantManager tenantManager;

	public ComplaintTests(@Autowired UserAccountManager userAccountManager, @Autowired TenantManager tenantManager) {
		this.userAccountManager = userAccountManager;
		this.tenantManager = tenantManager;
	}

	@BeforeEach
	void SetUp() {

		complaints = new Complaint(tenantManager.getAll().toList().get(0).getId(), tenantManager.getAll().toList().get(1).getId(),
				ComplaintState.PENDING, "Der ist zu laut");
	}

	@Test
	void getAuthor() {
		assertThat(complaints.getAuthor() == (tenantManager.getAll().toList().get(0).getId()));
	}

	@Test
	void getSubject() {
		assertThat(complaints.getSubject() == tenantManager.getAll().toList().get(1).getId());
	}


	@Test
	void getState() {
		assertThat(complaints.getState().equals(ComplaintState.PENDING));
	}

	@Test
	void getDescription() {
		assertThat(complaints.getDescription().equals("Der ist zu laut"));
	}

	@Test
	void setAuthor() {
		complaints.setAuthor(tenantManager.getAll().toList().get(2).getId());
		assertThat(complaints.getAuthor() == (tenantManager.getAll().toList().get(2).getId()));
	}

	@Test
	void setIllegalAuthor() {
		Long authorId = Long.valueOf(0);
		assertThrows(IllegalArgumentException.class, () -> complaints.setAuthor(authorId));
	}

	@Test
	void setSubject() {
		complaints.setSubject(tenantManager.getAll().toList().get(0).getId());
		assertThat(complaints.getSubject() == (tenantManager.getAll().toList().get(0).getId()));
	}

	@Test
	void setIllegalSubject() {
		Long subjectId = Long.valueOf(0);
		assertThrows(IllegalArgumentException.class, () -> complaints.setSubject(subjectId));
	}

	@Test
	void setState() {
		complaints.setState(ComplaintState.FINISHED);
		assertThat(complaints.getState().equals(ComplaintState.FINISHED));
	}

	@Test
	void setIllegalState() {
		ComplaintState state = null;
		assertThrows(IllegalArgumentException.class, () -> complaints.setState(state));
	}

	@Test
	void setDescription() {
		complaints.setDescription("Der ist doof.");
		assertThat(complaints.getDescription().equals("Der ist doof."));
	}

	@Test
	void setIllegalDescription() {
		String description = null;
		assertThrows(IllegalArgumentException.class, () -> complaints.setDescription(description));
	}
}
