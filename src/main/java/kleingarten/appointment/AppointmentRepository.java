package kleingarten.appointment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

	@Override
	Streamable<Appointment> findAll();
}
