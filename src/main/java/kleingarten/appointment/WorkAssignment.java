package kleingarten.appointment;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotStatus;
import net.bytebuddy.implementation.attribute.AnnotationAppender;
import org.apache.tomcat.jni.Local;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.core.SalespointIdentifier;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WorkAssignment {

	private @Id @GeneratedValue long id;

	private int workHours;
	private LocalDateTime date;

	@OneToMany
	private List<Plot> plots;

	private String title;

	private String description;

	/**
	 * Private constructor of class {@link WorkAssignment}, which is used by the Spring Framework
	 */

	private WorkAssignment(){}


	/**
	 * Constructor of class {@link WorkAssignment}
	 * @param plots is a list of {@link Plot} as ArrayList {@link ArrayList}
	 * @param date date of the {@link WorkAssignment} as LocalDateTime {@link LocalDateTime}
	 * @param description String that describes the{@link WorkAssignment}
	 * @param title String that gives the {@link WorkAssignment} a title
	 * @param workHours gives the {@link WorkAssignment} work hours as int
	 */

	public WorkAssignment(LocalDateTime date, int workHours, String title, String description, List<Plot> plots){
		this.plots = plots;
		this.date = date;
		this.description = description;
		this.title = title;
		this.workHours = workHours;
		this.plots = new ArrayList<>();
	}

	/**
	 * Getter for the date of a {@link WorkAssignment}
	 * @return date as LocalDatTime of type {@link LocalDateTime }
	 */
	public LocalDateTime getDate() {
		return date;
	}


	/**
	 * Getter for the work hours of a {@link WorkAssignment}
	 * @return workHours as int of type {@link Integer }
	 */

	public int getWorkHours() {
		return workHours;
	}

	/**
	 * Getter for the title of a {@link WorkAssignment}
	 * @return title as String of type {@link String }
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * Setter for the title of a {@link WorkAssignment}
	 * @param title size as String
	 */

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for the description of a {@link WorkAssignment}
	 * @return description as String of type {@link String }
	 */

	public String getDescription() {
		return description;
	}

	/**
	 * Setter for the description of a {@link WorkAssignment}
	 * @param description as String
	 */

	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * Setter for the workHours of a {@link WorkAssignment}
	 * @param workHours as int
	 */

	public void setWorkHours(int workHours) {
		this.workHours = workHours;
	}

	/**
	 * Getter for the plots of a {@link WorkAssignment}
	 * @return plots as List of type {@link List }
	 */

	public List<Plot> getPlots() {
		return plots;
	}

	/**
	 * Add a plot to a {@link WorkAssignment}
	 * @param plot as Plot {@link Plot}
	 */

	public void addPlot(Plot plot){
		plots.add(plot);
	}

	/**
	 * Check if the current plot is in {@link WorkAssignment}
	 * @return boolean as Boolean of type {@link Boolean }
	 */

	public boolean containsPlot(Plot plot){
		return(plots.contains(plot));
	}

	/**
	 * Remove a plot from a {@link WorkAssignment}
	 * @param plot as Plot {@link Plot}
	 */

	public void removePlot(Plot plot){
		for (Plot parzelle : plots) {
			if(plot == parzelle){
				plots.remove(parzelle);
			}
		}
	}


	/**
	 * Getter for the id of a {@link WorkAssignment}
	 * @return id as long of type {@link Long }
	 */

	public long getId() {
		return id;
	}
}
