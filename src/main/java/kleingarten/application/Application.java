package kleingarten.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Application {


	private @Id @GeneratedValue long id;
	
	private String firstName, lastName, email;
	private String plotId;
	//date
	
	private ApplicationState state;
	
	public Application() {
		state=ApplicationState.NEW;
	}
	
	public Application(String firstName, String lastName, String email, String plotId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.plotId = plotId;
		
		state = ApplicationState.NEW;
	}
	
	public void accept() {
		if(state == ApplicationState.NEW) {
			state = ApplicationState.ACCEPTED;
		}
	}
	
	public long getId() {
		return id;
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
	
	public ApplicationState getState() {
		return state;
	}
	
	public String getStateAsString() {
		switch (state) {
		case NEW:
			return "neu";
		case ACCEPTED:
			return "angenommen";
		case DENIED:
			return "abgelehnt";
		case HIDDEN:
			return "hidden";
			
		default:
			return "fehler";
		}
	}

	public void deny() {
		if(state == ApplicationState.NEW || state == ApplicationState.ACCEPTED) {
			state = ApplicationState.DENIED;
		}
	}
	
	void hide() {
		state = ApplicationState.HIDDEN;
	}
	
	public String toString() {
		return "Um "+plotId+" - "+firstName+" "+lastName+" - "+email;
	}
	
	
}
