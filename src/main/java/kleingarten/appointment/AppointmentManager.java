package kleingarten.appointment;

import org.springframework.data.util.Streamable;

public class AppointmentManager {

	private final AppointmentRepository times;

	public AppointmentManager(AppointmentRepository times){
		this.times = times;
	}

	public Streamable<Appointment> getAll(){
		return times.findAll();
	}
}
