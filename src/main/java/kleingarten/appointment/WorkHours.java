package kleingarten.appointment;

import kleingarten.plot.Plot;
import net.bytebuddy.implementation.attribute.AnnotationAppender;
import org.salespointframework.core.SalespointIdentifier;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WorkHours {
	private @Id @GeneratedValue long id;

	private int workHours;


	private SalespointIdentifier plotID;
	/*
	@OneToMany
	private List<Plot> plots = new ArrayList<>();
	*/

	@ManyToOne
	private Appointment appointment;

	public WorkHours(int workHours, Appointment appointment, SalespointIdentifier plotID){
		super();
		this.workHours = workHours;
		this.appointment = appointment;
		this.plotID = plotID;
	}


	public int getWorkHours() {
		return workHours;
	}

	public void setWorkHours(int workHours) {
		this.workHours = workHours;
	}

	public SalespointIdentifier getPlot() {
		return plotID;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
}
