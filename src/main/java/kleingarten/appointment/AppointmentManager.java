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

		return times.save((new Appointment(form.getTime(), form.getDate())));
		}

	}
