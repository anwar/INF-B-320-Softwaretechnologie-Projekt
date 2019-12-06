package kleingarten.appointment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface WorkAssignmentRepository extends CrudRepository<WorkAssignment, Long> {

	@Override
	List<WorkAssignment> findAll();
}
