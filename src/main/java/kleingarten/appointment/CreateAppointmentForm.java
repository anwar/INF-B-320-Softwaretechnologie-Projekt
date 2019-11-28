package kleingarten.appointment;

public class CreateAppointmentForm {

	private final String time;

	private final String date;

	public CreateAppointmentForm(String time, String date){
		this.time = time;
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}
}
