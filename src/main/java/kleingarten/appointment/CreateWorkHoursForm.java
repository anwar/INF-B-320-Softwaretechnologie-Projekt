package kleingarten.appointment;

public class CreateWorkHoursForm {

	private String workhours;

	private String time;

	private String date;


	public CreateWorkHoursForm(String workhours, String time, String date){
		this.workhours = workhours;
		this.time = time;
		this.date = date;
	}

	public String getWorkhours() {
		return workhours;
	}

	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}
}
