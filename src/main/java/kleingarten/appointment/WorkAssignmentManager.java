package kleingarten.appointment;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotService;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkAssignmentManager {


	private WorkAssignmentRepository workAssignmentRepository;
	private PlotService plotService;

	/**
	 * Constructor of class {@link WorkAssignmentManager}
	 *
	 * @param workAssignmentRepository repository of WorkAssignments as {@link WorkAssignmentRepository}
	 * @param plotService              is the Manager of the {@link Plot}s {@link PlotService}
	 */

	public WorkAssignmentManager(WorkAssignmentRepository workAssignmentRepository, PlotService plotService) {
		this.workAssignmentRepository = workAssignmentRepository;
		this.plotService = plotService;
	}

	/**
	 * function in {@link WorkAssignmentManager}
	 * its a Getter to get all the WorkAssignments {@link WorkAssignment} and take it from the repository {@link WorkAssignmentRepository}
	 * is sorted by current year {@link LocalDateTime}
	 *
	 * @return a list of all @{@link WorkAssignment}
	 */
	public List<WorkAssignment> getAll() {
		LocalDateTime localDateTime = LocalDateTime.now();
		List<WorkAssignment> workAssignments = new ArrayList<>();
		for (WorkAssignment workAssignment : workAssignmentRepository.findAll()) {
			if (workAssignment.getDate().getYear() >= localDateTime.getYear()) {
				workAssignments.add(workAssignment);
			}
		}
		return workAssignments;
	}


	/**
	 * function to create a {@link WorkAssignment} in {@link WorkAssignmentManager}
	 *
	 * @param form from type {@link CreateWorkAssignmentForm}
	 * @return a @{@link WorkAssignment}
	 */
	public WorkAssignment createAssignment(CreateWorkAssignmentForm form) {
		return workAssignmentRepository.save(new WorkAssignment(form.getDateTime(), 0, form.getTitle(),
				form.getDescription(), null));
	}

	/**
	 * function to create a {@link WorkAssignment} in {@link WorkAssignmentManager} for the {@link kleingarten.configuration.AppDataInitializer}
	 *
	 * @param date        from type {@link LocalDateTime}
	 * @param workHours   from type {@link Integer}
	 * @param title       from type {@link String}
	 * @param description from type {@link String}
	 * @param plots       from type {@link List} from {@link Plot}
	 * @return a @{@link WorkAssignment}
	 */
	public WorkAssignment createAssignmentForInitializer(LocalDateTime date, int workHours, String title,
														 String description, List<Plot> plots) {
		return workAssignmentRepository.save(new WorkAssignment(date, workHours, title, description, plots));
	}

	/**
	 * The function gets @param localDateTime and checks if it is already in the list and
	 * @return a Boolean
	 */
	public boolean containsListTheDate(LocalDateTime localDateTime) {
		for (WorkAssignment workAssignment : workAssignmentRepository.findAll()) {
			if (workAssignment.getDate().equals(localDateTime)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * The function gets @param workAssigmentID and @return a WorkAssignment
	 */
	public WorkAssignment findByID(long workAssigmentID) {
		for (WorkAssignment workAssignment : workAssignmentRepository.findAll()) {
			if (workAssignment.getId() == workAssigmentID) {
				return workAssignment;
			}
		}
		return null;
	}


	/**
	 * The function gets @param plotID and @return a Plot
	 */
	public Plot findByID(ProductIdentifier plotID) {
		return plotService.findById(plotID);
	}


	/**
	 * The function gets a @param plotID and @param workAssignmentID and
	 * adds the plot to the list. (Plot from plotID)
	 */
	public void addPlotToWorkAssignment(ProductIdentifier plotID, long workAssignmentID) {
		WorkAssignment workAssignment = findByID(workAssignmentID);
		Plot plot = findByID(plotID);
		if (!workAssignment.containsPlot(plot)) {
			workAssignment.addPlot(plot);
			workAssignmentRepository.save(workAssignment);
		}
	}

	/**
	 * The function gets @param plotID and @param workAssignmentID and
	 * remove the plot from the list. (Plot from plotID)
	 */
	public void removePlotOutWorkAssignment(ProductIdentifier plotID, long workAssignmentID) {
		WorkAssignment workAssignment = findByID(workAssignmentID);
		Plot plot = findByID(plotID);
		if (workAssignment.containsPlot(plot)) {
			workAssignment.removePlot(plot);
			workAssignmentRepository.save(workAssignment);
		}
	}

	/**
	 * The function gets a @param plotID and @return all WorkAssignment's for a Plot.
	 */
	public List<WorkAssignment> getForPlotWorkAssignments(ProductIdentifier plotID) {
		Plot plot = findByID(plotID);
		List<WorkAssignment> buffer = new ArrayList<>();
		for (WorkAssignment workAssignment : workAssignmentRepository.findAll()) {
			if (workAssignment.containsPlot(plot)) {
				buffer.add(workAssignment);
			}
		}
		return buffer;
	}

	/**
	 * The function gets @param workHours and @param workAssigmentID and set the workHours for
	 * a @param WorkAssignemnt
	 */
	public void setWorkHours(int workHours, long workAssigmentID) {

		WorkAssignment workAssignment = findByID(workAssigmentID);

		workAssignment.setWorkHours(workHours);

		workAssignmentRepository.save(workAssignment);
	}

	/**
	 * The function gets @param plotID and @return the WorkHours from a Plot
	 */
	public int getWorkHours(ProductIdentifier plotID) {
		Plot plot = findByID(plotID);
		int actualYear = LocalDateTime.now().getYear();
		List<WorkAssignment> buffer = new ArrayList<>();

		for (WorkAssignment workAssignment : workAssignmentRepository.findAll()) {
			if (workAssignment.containsPlot(plot) && workAssignment.getDate().getYear() == actualYear) {
				buffer.add(workAssignment);
			}
		}

		int sumOfWorkHours = 0;

		for (WorkAssignment workAssignment : buffer) {
			sumOfWorkHours += workAssignment.getWorkHours();
		}
		return sumOfWorkHours;
	} //nochmal überarbeiten

	/**
	 * The function gets @param workAssignmentID and @return a list of Plots from one WorkAssignment
	 */
	public List<Plot> getPlotsInWorkAssignment(long workAssignmentID) {

		List<Plot> plotList = new ArrayList<>();

		for (WorkAssignment workAssignment : workAssignmentRepository.findAll()) {
			if (workAssignment.getId() == workAssignmentID) {
				for (String plotId : workAssignment.getPlots()) {
					plotList.add(plotService.findById(plotId));
				}
			}
		}
		return plotList;
	}

	/**
	 * The function gets @param WorkAssignmentID and remove a WorkAssignment
	 */
	public void removeWorkAssignment(long WorkAssignmentID) {
		WorkAssignment workAssignment = findByID(WorkAssignmentID);

		workAssignmentRepository.delete(workAssignment);
	}

}
