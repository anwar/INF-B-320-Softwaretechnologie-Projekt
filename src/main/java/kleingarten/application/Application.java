package kleingarten.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Application {


	private @Id @GeneratedValue long id;
	
	String firstName, lastName, email;
	String plotId;
	//date
	
	ApplicationState state;
	
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
	
	public void deny() {
		if(state == ApplicationState.NEW) {
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
