package kleingarten.appointment;

import org.apache.tomcat.jni.Local;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class AppointmentManager {

	private final AppointmentRepository times;

	public AppointmentManager(AppointmentRepository times){
		this.times = times;
	}

	public Streamable<Appointment> getAll(){
		return times.findAll();
	}

	public Appointment createAppointment(CreateAppointmentForm form){

		DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
		LocalDate date = LocalDate.parse(form.getDate());
		date.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
		LocalTime time = LocalTime.parse(form.getTime());

		return times.save((new Appointment(time, date)));

	}

}
