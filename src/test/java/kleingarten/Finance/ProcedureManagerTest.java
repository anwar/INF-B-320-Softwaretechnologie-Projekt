package kleingarten.Finance;


import static org.assertj.core.api.Assertions.*;

//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.junit4.SpringRunner;

import kleingarten.plot.Plot;

// https://www.baeldung.com/spring-boot-testing

//@RunWith(SpringRunner.class)
public class ProcedureManagerTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ProcedureManager procedureManager;


	//@Test
	public void findProcedureByIdTest() {
		Plot plot = new Plot("sandkasten", 10, "Vorsicht: Kinder spielen hier.");
		Procedure p1 = new Procedure( 2018, plot, 1l );

		entityManager.persist(p1);
		entityManager.flush();

		Procedure p1Found = procedureManager.get(p1.getId());

		assertThat(p1Found.getId()).isEqualTo(p1.getId());

	}
}
