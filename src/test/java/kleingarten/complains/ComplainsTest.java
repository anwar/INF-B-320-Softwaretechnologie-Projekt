 package kleingarten.complains;


import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class ComplainsTest {

	private Complains complains;
	private final UserAccountManager userAccountManager;
	private Plot authorPlot;
	private  Plot subjectPlot;
	private Tenant author;
	private Tenant subject;

	public ComplainsTest(@Autowired UserAccountManager userAccountManager){
		this.userAccountManager = userAccountManager;
	}

	@BeforeEach
	void SetUp(){

		author = new Tenant("Jassi", "Gepackert", "Neben Isa und Francy",
			"908964875734", "13.05.1999", userAccountManager.create("jassi", Password.UnencryptedPassword.of("123"),"jassis@email.com", Role.of("Hauptpächter")));
		subject = new Tenant("Der", "Behrens", "über meinem Bruder", "93596164", "14.09.1900",
			userAccountManager.create("behrens", Password.UnencryptedPassword.of("123"), "behrens@mail.de", Role.of("Hauptpächter")));
		authorPlot = new Plot("1", 25, "kleine Parzelle");
		subjectPlot = new Plot("2", 30, "große Parzelle");
		//complains = new Complains(author.getId(), subject.getId(), authorPlot.getId(), subjectPlot.getId(),ComplainsState.PENDING, "Der ist zu laut.");
	}

/*
	@Test
	void getAuthor() {
		assertThat(complains.getAuthor().getUserAccount().getUsername().equals("jassi"));
	} */

	@Test
	void getSubject() {
	}

	@Test
	void getAuthorPlot() {
	}

	@Test
	void getSubjectPlot() {
	}

	@Test
	void getState() {
	}

	@Test
	void setAuthor() {
	}

	@Test
	void setSubject() {
	}

	@Test
	void setAuthorPlot() {
	}

	@Test
	void setSubjectPlot() {
	}

	@Test
	void setState() {
	}

	@Test
	void getAuthorName() {
	}

	@Test
	void getSubjectName() {
	}
}
