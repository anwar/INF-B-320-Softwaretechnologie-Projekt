package kleingarten.appointment;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotCatalog;
import org.apache.tomcat.jni.Local;
import org.hibernate.jdbc.Work;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WorkAssignmentManager {

	private WorkAssignmentRepository workAssignmentRepository;

	public WorkAssignmentManager(WorkAssignmentRepository workAssignmentRepository){
		this.workAssignmentRepository = workAssignmentRepository;
	}


	public List<WorkAssignment> getAll(){
		LocalDateTime localDateTime = LocalDateTime.now();
		List<WorkAssignment> workAssignments = new ArrayList<>();
		for (WorkAssignment workAssignment:workAssignmentRepository.findAll()) {
			if(workAssignment.getDate().getYear() >= localDateTime.getYear()){
				workAssignments.add(workAssignment);
			}
		}
		return workAssignments;
	}

	public WorkAssignment createAssignment(CreateWorkAssignmentForm form){


		return workAssignmentRepository.save(new WorkAssignment(form.getDateTime(), 0, form.getTitle(), form.getDescription(), null));
	}

	public WorkAssignment createAssignmentForInitializer(LocalDateTime date, String title, String description){
		return workAssignmentRepository.save(new WorkAssignment(date, 0,title, description, null));
	}

	public boolean containsListTheDate(LocalDateTime localDateTime){
		for (WorkAssignment workAssignment: workAssignmentRepository.findAll()) {
			if(workAssignment.getDate().equals(localDateTime)) {
				return true;
			}
		}
		return false;
	}

}
