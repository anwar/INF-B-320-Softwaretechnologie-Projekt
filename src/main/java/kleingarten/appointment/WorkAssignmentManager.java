package kleingarten.appointment;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotCatalog;
import kleingarten.plot.PlotService;
import kleingarten.tenant.Tenant;
import org.apache.tomcat.jni.Local;
import org.hibernate.jdbc.Work;
import org.salespointframework.catalog.ProductIdentifier;
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
	private PlotCatalog plotCatalog;
	private PlotService plotService;

	public WorkAssignmentManager(WorkAssignmentRepository workAssignmentRepository, PlotCatalog plotCatalog, PlotService plotService){
		this.workAssignmentRepository = workAssignmentRepository;
		this.plotService = plotService;
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

	public WorkAssignment findByID(long workAssigmentID){
		for (WorkAssignment workAssignment : workAssignmentRepository.findAll()) {
			if(workAssignment.getId() == workAssigmentID){
				return workAssignment;
			}
		}
		return null;
	}

	public Plot findbyID(ProductIdentifier identifier){

		Plot findPlotByID = plotService.findById(identifier);
		for (Plot plot: plotCatalog.findAll()) {
			if(plot == findPlotByID){
				return plot;
			}
		}

		return null;
	}

	public boolean addWorkAssignment(ProductIdentifier identifier, long workAssigmentID){
		Plot plot = findbyID(identifier);
		WorkAssignment workAssignment = findByID(workAssigmentID);

		if(!workAssignment.containsPlot(plot)){
			workAssignment.addPlot(plot);
			return true;
		}
		return false;
	}

	public boolean removeWorkAssignment(ProductIdentifier identifier, long workAssigmentID){
		Plot plot = findbyID(identifier);
		WorkAssignment workAssignment = findByID(workAssigmentID);

		if(!workAssignment.containsPlot(plot)){
			workAssignment.removePlot(plot);
			return true;
		}
		return false;
	}

	public void setWorkHours(int workHours, long workAssigmentID){
		WorkAssignment workAssignment = findByID(workAssigmentID);

		workAssignment.setWorkHours(workHours);
	}

}
