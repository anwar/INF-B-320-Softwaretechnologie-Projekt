package kleingarten.appointment;

import org.apache.tomcat.jni.Local;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

		LocalTime localTime = LocalTime.parse(form.getTime());
		LocalDate localDate = LocalDate.parse(form.getDate());
		localDate.format(DateTimeFormatter.ofPattern("DD/MM/YYYY"));

		return times.save((new Appointment(localTime, localDate)));
		}

	public Appointment createAppointmentForInitializer(LocalTime time, LocalDate date){
		return times.save((new Appointment(time, date)));
	}
	}
