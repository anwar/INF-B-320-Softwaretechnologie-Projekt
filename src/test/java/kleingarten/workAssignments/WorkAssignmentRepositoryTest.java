package kleingarten.workAssignments;

import kleingarten.appointment.WorkAssignment;
import kleingarten.appointment.WorkAssignmentRepository;
import kleingarten.news.NewsEntry;
import kleingarten.news.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
		WorkAssignment workAssignment = workAssignmentRepository.save(new WorkAssignment(LocalDateTime.of(2020,1,1,1,0), 0, "Test", "Test", null));
		assertThat(workAssignmentRepository.findAll()).contains(workAssignment);
	}

}
