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

	@ElementCollection
	private List<String> plots;

	private String title;

	private String description;

	private WorkAssignment(){}

	public WorkAssignment(LocalDateTime date, int workHours, String title, String description, List<Plot> plots){
		this.plots = new ArrayList<String>();
		
		
		if(plots!=null)
		for(Plot p : plots) {
			if(p == null) continue;
			this.plots.add(p.getId().toString());
		}
		
		
		this.date = date;
		this.description = description;
		this.title = title;
		this.workHours = workHours;
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

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setWorkHours(int workHours) {
		this.workHours = workHours;
	}

	public List<String> getPlots() {
		return plots;
	}

	public void addPlot(Plot plot){
		plots.add(plot.getId().toString());
	}

	public boolean containsPlot(Plot plot){
		return plots.contains(plot.getId().toString());
	}

	public void removePlot(Plot plot){
		System.out.println("Remove plot nr. "+plot.getName());
		
		int i = 0;
		
		for (; i<plots.size();) {
			String parzelle = plots.get(i);
			System.out.println(parzelle + " - " + plot.getId());
			if(plot.getId().toString().equalsIgnoreCase(parzelle)){
				plots.remove(i);
			} else {
				i++;
			}
		}
	}

	public long getId() {
		return id;
	}
}
