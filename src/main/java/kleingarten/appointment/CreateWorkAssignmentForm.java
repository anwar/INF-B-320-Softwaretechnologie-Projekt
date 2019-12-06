package kleingarten.appointment;

public class CreateWorkAssignmentForm {

	private final String date;

	private final String time;

	private final String title;

	private final String description;

	public CreateWorkAssignmentForm(String date, String time, String title, String description, String workHours){
		this.time = time;
		this.date = date;
		this.title = title;
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
}
