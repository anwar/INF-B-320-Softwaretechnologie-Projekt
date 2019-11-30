package kleingarten.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import kleingarten.plot.Plot;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Entity
public class Appointment {

	private @Id @GeneratedValue long id;


	private LocalTime time;
	@JsonFormat(pattern="dd/MM/yyyy")
	private LocalDate date;

	private Appointment() {}

	public Appointment(LocalTime time, LocalDate date){
		this.time = time;
		this.date = date;
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
}
