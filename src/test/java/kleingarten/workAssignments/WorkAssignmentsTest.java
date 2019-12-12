package kleingarten.workAssignments;

import kleingarten.appointment.WorkAssignment;
import kleingarten.plot.Plot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class WorkAssignmentsTest {

	private WorkAssignment workAssignment;
	private Plot plot;

	/**
	 * Set up for a mock {@link WorkAssignment} for each testing
	 */
	@BeforeEach
	public void setUp(){
		workAssignment = new WorkAssignment(LocalDateTime.now(), 0,"Test", "Test", null);
	}

	@Test
	public void initialLocalDateTest(){
		assertThat(workAssignment.getDate());
	}

	/**
	 * Test for the initialization of the workHours
	 */

	@Test
	public void initialWorkHoursTest(){
		assertThat(workAssignment.getWorkHours());
	}

	/**
	 * Test for the Setter of the workhours
	 */

	@Test
	public void setWorkHoursTest(){
		workAssignment.setWorkHours(12);
		assertThat(workAssignment.getWorkHours() == 12);
	}

	/**
	 * Test for the Getter of the workHours
	 */
	@Test
	public void getWorkHoursTest(){
		assertThat(workAssignment.getWorkHours() == 0);
	}

	/**
	 * Test for the initialization of the Title
	 */
	@Test
	public void initialTitleTest(){
		assertThat(workAssignment.getTitle());
	}

	/**
	 * Test for the Setter of the Title
	 */
	@Test
	public void setTitleTest(){
		workAssignment.setTitle("Test");
		assertThat(workAssignment.getTitle().equals("Test"));
	}

	/**
	 * Test for the Getter of the Title
	 */
	@Test
	public void getTitleTest(){
		assertThat(workAssignment.getTitle().equals("Test"));
	}


	/**
	 * Test for the initialization of the Description
	 */
	@Test
	public void initialDescriptionTest(){
		assertThat(workAssignment.getDescription());
	}

	/**
	 * Test for the Setter of the Description
	 */
	@Test
	public void setDescriptionTest(){
		workAssignment.setDescription("Test");
		assertThat(workAssignment.getDescription().equals("Test"));
	}

	/**
	 * Test for the Getter of the Description
	 */
	@Test
	public void getDescription(){
		assertThat(workAssignment.getDescription().equals("Test"));
	}

	@Test
	public void initialIDTest(){
		assertThat(workAssignment.getId());
	}

	@Test
	public void getIDTest(){
		assertThat(workAssignment.getId());
	}

	@Test
	public void initialGetPlotsTest(){
		assertThat(workAssignment.getPlots());
	}

	@Test
	public void initialContainsPlotTest(){
		assertThat(workAssignment.containsPlot(plot));
	}
}
