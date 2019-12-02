package kleingarten.appointment;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotCatalog;
import org.hibernate.jdbc.Work;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class WorkHoursManager {

	private WorkHoursRepository workHoursRepository;
	private PlotCatalog plotCatalog;

	public WorkHoursManager(WorkHoursRepository workHoursRepository,PlotCatalog plotCatalog){
		this.workHoursRepository = workHoursRepository;
		this.plotCatalog = plotCatalog;

	}

	public Streamable<WorkHours> getAll(){
		return workHoursRepository.findAll();
	}

	public WorkHours addAppointment(AddAppointmentForm form, SalespointIdentifier plotID){

		int workHours = Integer.parseInt(form.getWorkHours());
		LocalDate localDate = LocalDate.parse(form.getDate());
		LocalTime localTime = LocalTime.parse(form.getTime());
		localDate.format(DateTimeFormatter.ofPattern("DD/MM/YYYY"));
		Appointment appointment = new Appointment(localTime,localDate);

		return workHoursRepository.save(new WorkHours(0, appointment, plotID));
	}


}
