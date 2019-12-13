package kleingarten.appointment;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotCatalog;
import kleingarten.plot.PlotService;
import org.apache.catalina.filters.RemoteIpFilter;
import org.apache.tomcat.jni.Local;
import org.hibernate.jdbc.Work;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkAssignmentManager {


	private WorkAssignmentRepository workAssignmentRepository;
	private PlotService plotService;

	public WorkAssignmentManager(WorkAssignmentRepository workAssignmentRepository, PlotService plotService){
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

	public WorkAssignment createAssignmentForInitializer(LocalDateTime date, int workHours, String title, String description, List<Plot> plots){
		return workAssignmentRepository.save(new WorkAssignment(date, workHours,title, description, plots));
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

	public Plot findByID(ProductIdentifier plotID) {
		return plotService.findById(plotID);
	}


	public void addPlotToWorkAssignment(ProductIdentifier plotID, long workAssignmentID){
		WorkAssignment workAssignment = findByID(workAssignmentID);
		System.out.println(workAssignment);
		Plot plot = findByID(plotID);
		if(!workAssignment.containsPlot(plot)){
			workAssignment.addPlot(plot);
			workAssignmentRepository.save(workAssignment);
		}
	}

	public void removePlotOutWorkAssignment(ProductIdentifier plotID, long workAssignmentID){
		WorkAssignment workAssignment = findByID(workAssignmentID);
		Plot plot = findByID(plotID);

		if(workAssignment.containsPlot(plot)){
			workAssignment.removePlot(plot);
			workAssignmentRepository.save(workAssignment);
		}
	}


	public List<WorkAssignment> getForPlotWorkAssignments(ProductIdentifier plotID){
		Plot plot = findByID(plotID);
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

		workAssignmentRepository.save(workAssignment);
	}

	public int getWorkHours(ProductIdentifier plotID, long workAssignmentID){
		Plot plot = findByID(plotID);
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
	} //nochmal überarbeiten

	public List<Plot> getPlotsInWorkAssignment(long workAssignmentID){

		List<Plot> plotList = new ArrayList<>();

		for (WorkAssignment workAssignment: workAssignmentRepository.findAll()) {
			if(workAssignment.getId() == workAssignmentID){
				plotList = workAssignment.getPlots();
			}
		}
		return plotList;
	}

}
