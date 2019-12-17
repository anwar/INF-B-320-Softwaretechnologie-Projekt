package kleingarten.application;

public class CreateApplicationForm {

	private final String firstName, lastName, email;
	private String plotId;

	public CreateApplicationForm(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPlotId() {
		return plotId;
	}

	public void setPlotId(String plotId) {
		this.plotId = plotId;
	}


}
