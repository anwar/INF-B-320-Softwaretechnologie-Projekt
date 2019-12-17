package kleingarten.workAssignments;

import kleingarten.appointment.WorkAssignment;
import kleingarten.appointment.WorkAssignmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class WorkAssignmentRepositoryTest {
	@Autowired
	WorkAssignmentRepository workAssignmentRepository;

	/**
	 * Test the persistence of {@link WorkAssignmentRepository}.
	 */

	@Test
	void newsEntryPersistence() {
		WorkAssignment workAssignment = workAssignmentRepository.save(new WorkAssignment(LocalDateTime.of(2020, 1, 1, 1, 0), 0, "Test", "Test", null));
		assertThat(workAssignmentRepository.findAll()).contains(workAssignment);
	}

}
