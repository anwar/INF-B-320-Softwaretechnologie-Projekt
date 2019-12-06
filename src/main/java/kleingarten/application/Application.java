package kleingarten.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Application {


	private @Id @GeneratedValue long id;
	
	String name, email;
	String plotId;
	//date
	
	ApplicationState state;
	
	public Application(String name, String email, String plotId) {
		this.name = name;
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
	
	
}
