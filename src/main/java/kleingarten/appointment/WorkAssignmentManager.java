package kleingarten.appointment;

import kleingarten.plot.Plot;
import org.apache.catalina.filters.RemoteIpFilter;
import org.apache.tomcat.jni.Local;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
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

	public WorkAssignment createAssignmentForInitializer(LocalDateTime date, String title, String description, List<Plot> plots){
		return workAssignmentRepository.save(new WorkAssignment(date, 0,title, description, plots));
	}

	public boolean containsListTheDate(LocalDateTime localDateTime){
		for (WorkAssignment workAssignment: workAssignmentRepository.findAll()) {
			if(workAssignment.getDate().equals(localDateTime)) {
				return true;
			}
		}
		return false;
	}

	public WorkAssignment findByID(long workAssigmentID){
		for (WorkAssignment workAssignment : workAssignmentRepository.findAll()) {
			if(workAssignment.getId() == workAssigmentID){
				return workAssignment;
			}
		}
		return null;
	}

	public boolean addPlotToWorkAssignment(Plot plot, long workAssigmentID){
		WorkAssignment workAssignment = findByID(workAssigmentID);

		if(!workAssignment.containsPlot(plot)){
			workAssignment.addPlot(plot);
			return true;
		}
		return false;
	}

	public boolean removePlotOutWorkAssignment(Plot plot, long workAssigmentID){
		WorkAssignment workAssignment = findByID(workAssigmentID);

		if(workAssignment.containsPlot(plot)){
			workAssignment.removePlot(plot);
			return true;
		}
		return false;
	}

	public List<WorkAssignment> getForPlotWorkAssignments(Plot plot){
		List<WorkAssignment> buffer = new ArrayList<>();
		for(WorkAssignment workAssignment: workAssignmentRepository.findAll()){
			if(workAssignment.containsPlot(plot)){
				buffer.add(workAssignment);
			}
		}
		return buffer;
	}

	public void setWorkHours(int workHours, long workAssigmentID){
		WorkAssignment workAssignment = findByID(workAssigmentID);

		workAssignment.setWorkHours(workHours);
	}

	public int getWorkAssignment(Plot plot, long workAssignmentID){

		int actualYear = LocalDateTime.now().getYear();
		List<WorkAssignment> buffer = new ArrayList<>();

		for(WorkAssignment workAssignment: workAssignmentRepository.findAll()){
			if(workAssignment.containsPlot(plot) && workAssignment.getDate().getYear() == actualYear){
				buffer.add(workAssignment);
			}
		}

		int sumOfWorkHours = 0;

		for(WorkAssignment workAssignment : buffer){
			sumOfWorkHours += workAssignment.getWorkHours();
		}
		return sumOfWorkHours;
	}

}
