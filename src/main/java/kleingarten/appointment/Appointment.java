package kleingarten.appointment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Appointment {

	private @Id @GeneratedValue long id;

	private String time;
	private Date date;

	public Appointment(String time, Date date){
		this.time = time;
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
