package kleingarten.appointment;

import kleingarten.plot.Plot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.List;

public interface WorkAssignmentRepository extends CrudRepository<WorkAssignment, Long> {

	/**
	 * Return all {@link WorkAssignment}s
	 * @return WorkAssignment as {@link List} of {@link WorkAssignment}
	 */
	@Override
	List<WorkAssignment> findAll();
}
