package kleingarten.workAssignments;

import kleingarten.appointment.WorkAssignment;
import kleingarten.appointment.WorkAssignmentManager;
import kleingarten.appointment.WorkAssignmentRepository;
import kleingarten.plot.Plot;
import kleingarten.tenant.TenantManager;
import kleingarten.tenant.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.Assert.assertThat;

@SpringBootTest
@Transactional
public class WorkAssignmentManagerTest {

	private WorkAssignment workAssignment;
	private Plot plot;
	private WorkAssignmentManager workAssignmentManager;
	private WorkAssignmentRepository workAssignmentRepository;

	public WorkAssignmentManagerTest(@Autowired WorkAssignmentManager workAssignmentManager, @Autowired WorkAssignmentRepository workAssignmentRepository) {
		this.workAssignmentManager = workAssignmentManager;
		this.workAssignmentRepository = workAssignmentRepository;
	}

	@BeforeEach
	public void setUp(){
		workAssignment = new WorkAssignment(LocalDateTime.now(), 0,"Test", "Test", null);
		plot= new Plot("test", 0,"test");
		workAssignmentRepository.save(workAssignment);
	}



}
