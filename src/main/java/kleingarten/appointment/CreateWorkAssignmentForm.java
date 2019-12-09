package kleingarten.appointment;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateWorkAssignmentForm {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime time;

	private final String title;

	private final String description;

	public CreateWorkAssignmentForm(LocalDate date, LocalTime time, String title, String description, String workHours){
		this.time = time;
		this.date = date;
		this.title = title;
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}
	public LocalTime getTime() {
		return time;
	}

	public LocalDateTime getDateTime(){
		return LocalDateTime.of(date, time);
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
}
