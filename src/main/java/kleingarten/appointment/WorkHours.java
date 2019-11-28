package kleingarten.appointment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WorkHours {
	private @Id
	@GeneratedValue
	long id;

	public WorkHours(){

	}
}
