package kleingarten.appointment;

import kleingarten.plot.PlotCatalog;
import org.apache.tomcat.jni.Local;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class WorkAssignmentManager {

	private WorkAssignmentRepository workAssignmentRepository;

	public WorkAssignmentManager(WorkAssignmentRepository workAssignmentRepository){
		this.workAssignmentRepository = workAssignmentRepository;

	}
	public List<WorkAssignment> getAll(){
		return workAssignmentRepository.findAll();
	}

	public WorkAssignment createAssignment(CreateWorkAssignmentForm form){

		String date = form.getDate() + " " + form.getTime();

		return workAssignmentRepository.save(new WorkAssignment(LocalDateTime.parse(date), 0, form.getTitle(), form.getDescription()));
	}

	public WorkAssignment createAssignmentForInitializer(LocalDateTime date, String title, String description){
		return workAssignmentRepository.save(new WorkAssignment(date, 0,title, description));
	}


}
