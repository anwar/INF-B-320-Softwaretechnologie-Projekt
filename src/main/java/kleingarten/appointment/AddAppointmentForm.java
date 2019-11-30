package kleingarten.appointment;

public class AddAppointmentForm {

	private final String date;

	private final String time;

	private final String workHours;

	public AddAppointmentForm(String date, String time, String workHours){
		this.time = time;
		this.date = date;
		this.workHours = workHours;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getWorkHours() {
		return workHours;
	}
}
