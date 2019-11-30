package kleingarten.appointment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface WorkHoursRepository extends CrudRepository<WorkHours, Long> {

	@Override
	Streamable<WorkHours> findAll();
}
