package kleingarten.appointment;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class WorkHoursManager {

	private WorkHoursRepository workHoursRepository;

	public WorkHoursManager(WorkHoursRepository workHoursRepository){
		this.workHoursRepository = workHoursRepository;
	}

	public Streamable<WorkHours> getAll(){
		return workHoursRepository.findAll();
	}

	public WorkHours addAppointment(AddAppointmentForm form){

		int workHours = Integer.parseInt(form.getWorkHours());
		LocalDate localDate = LocalDate.parse(form.getDate());
		LocalTime localTime = LocalTime.parse(form.getTime());
		localDate.format(DateTimeFormatter.ofPattern("DD/MM/YYYY"));
		Appointment appointment = new Appointment(localTime,localDate);

		return workHoursRepository.save(new WorkHours(workHours, appointment));
	}

}
