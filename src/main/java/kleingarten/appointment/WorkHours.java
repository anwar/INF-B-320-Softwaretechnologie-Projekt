package kleingarten.appointment;

import kleingarten.plot.Plot;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WorkHours {
	private @Id @GeneratedValue long id;

	private int workHours;

	@OneToMany
	private List<Plot> plots = new ArrayList<>();

	@ManyToOne
	private Appointment appointment;

	public WorkHours(int workHours, Appointment appointment){
		super();
		this.workHours = workHours;
		this.appointment = appointment;
	}


	public int getWorkHours() {
		return workHours;
	}

	public void setWorkHours(int workHours) {
		this.workHours = workHours;
	}

	public void addPlot(Plot plot){
		plots.add(plot);
	}

	public Iterable<Plot> getPlots(){
		return plots;
	}

	public Appointment getAppointment() {
		return appointment;
	}
}
