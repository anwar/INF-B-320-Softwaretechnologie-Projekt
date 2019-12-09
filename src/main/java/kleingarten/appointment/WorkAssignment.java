package kleingarten.appointment;

import kleingarten.plot.Plot;
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

	private WorkAssignment(){}

	public WorkAssignment(LocalDateTime date, int workHours, String title, String description, List<Plot> plots){
		this.plots = plots;
		this.date = date;
		this.description = description;
		this.title = title;
		this.workHours = workHours;
		this.plots = new ArrayList<>();
	}


	public LocalDateTime getDate() {
		return date;
	}

	public int getWorkHours() {
		return workHours;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setWorkHours(int workHours) {
		this.workHours = workHours;
	}

	public List<Plot> getPlots() {
		return plots;
	}

	public void addPlot(Plot plot){
		plots.add(plot);
	}

	public boolean containsPlot(Plot plot){
		return(plots.contains(plot));
	}

	public void removePlot(Plot plot){
		for (Plot parzelle : plots) {
			if(plot == parzelle){
				plots.remove(parzelle);
			}
		}
	}

	public long getId() {
		return id;
	}
}
