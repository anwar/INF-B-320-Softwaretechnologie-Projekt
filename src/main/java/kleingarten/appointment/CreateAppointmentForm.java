package kleingarten.appointment;

public class CreateAppointmentForm {

	private final String time;

	private final String date;

	private final String workHours;

	public CreateAppointmentForm(String time, String date , String workHours){
		this.time = time;
		this.date = date;
		this.workHours = workHours;
	}

	public String getWorkHours() {
		return workHours;
	}

	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}
}
