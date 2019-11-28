package kleingarten.appointment;

import kleingarten.plot.Plot;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Entity
public class Appointment {

	private @Id @GeneratedValue long id;

	private ArrayList<Plot> plots;
	private LocalTime time;
	private LocalDate date;

	private Appointment() {

	}

	public Appointment(LocalTime time, LocalDate date){
		this.time = time;
		this.date = date;
		plots = new ArrayList<>();
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public ArrayList<Plot> getPlots() {
		return plots;
	}

	public void setPlots(ArrayList<Plot> plots) {
		this.plots = plots;
	}
}
