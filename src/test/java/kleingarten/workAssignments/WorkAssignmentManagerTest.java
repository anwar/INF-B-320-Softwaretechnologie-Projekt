package kleingarten.workAssignments;

import kleingarten.appointment.WorkAssignment;
import kleingarten.appointment.WorkAssignmentManager;
import kleingarten.plot.Plot;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class WorkAssignmentManagerTest {

	private WorkAssignment workAssignment;
	private Plot plot;
	private WorkAssignmentManager workAssignmentManager;

	@BeforeEach
	public void setUp(){

		workAssignment = new WorkAssignment(LocalDateTime.now(), 0,"Test", "Test", null);
		plot= new Plot("test", 0,"test");
	}

	@Test
	public void addPlotToWorkAssignment(){
		workAssignmentManager.addPlotToWorkAssignment(plot.getId(), workAssignment.getId());
	}
}
