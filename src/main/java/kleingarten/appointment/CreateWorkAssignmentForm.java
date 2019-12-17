package kleingarten.appointment;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateWorkAssignmentForm {

	private final String title;
	private final String description;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime time;

	/**
	 * Constructor of class {@link CreateWorkAssignmentForm}
	 *
	 * @param time        is a LocalTime of {@link LocalTime}
	 * @param date        is a LocalDate of {@link LocalDate}
	 * @param title       String that gives the {@link WorkAssignment} a title
	 * @param description String that describes the{@link WorkAssignment}
	 * @param workHours   String that describes the work hours
	 */

	public CreateWorkAssignmentForm(LocalDate date, LocalTime time, String title, String description, String workHours) {
		this.time = time;
		this.date = date;
		this.title = title;
		this.description = description;
	}

	/**
	 * Getter for the date of a {@link CreateWorkAssignmentForm}
	 *
	 * @return date as LocalDate of type {@link LocalDate }
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Getter for the time of a {@link CreateWorkAssignmentForm}
	 *
	 * @return time as LocalTime of type {@link LocalTime }
	 */
	public LocalTime getTime() {
		return time;
	}

	/**
	 * Getter for the localDateTime of a {@link CreateWorkAssignmentForm}
	 *
	 * @return localDateTime as LocalDatTime of type {@link LocalDateTime }
	 */

	public LocalDateTime getDateTime() {
		return LocalDateTime.of(date, time);
	}

	/**
	 * Getter for the title of a {@link CreateWorkAssignmentForm}
	 *
	 * @return title as String of type {@link String }
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * Getter for the description of a {@link CreateWorkAssignmentForm}
	 *
	 * @return description as String of type {@link String }
	 */

	public String getDescription() {
		return description;
	}
}
