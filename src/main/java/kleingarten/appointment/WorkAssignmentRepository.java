package kleingarten.appointment;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WorkAssignmentRepository extends CrudRepository<WorkAssignment, Long> {

	/**
	 * Return all {@link WorkAssignment}s
	 *
	 * @return WorkAssignment as {@link List} of {@link WorkAssignment}
	 */
	@Override
	List<WorkAssignment> findAll();
}
