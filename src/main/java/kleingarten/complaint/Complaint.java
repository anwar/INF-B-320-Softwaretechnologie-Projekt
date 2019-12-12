package kleingarten.complaint;


// WIP we still have to do this, but if someone got time, they can start working on this

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Class to specify {@link Complaint}
 */

@Entity
public class Complaint {


	public long authorId;
	public long subjectId;
	public ComplaintState state;
	public String description;
	private @Id
	@GeneratedValue
	long id;


	/**
	 * Private constructor of class {@link Complaint}, which is used by the Spring Framework
	 */
	private Complaint() {
	}

	/**
	 * @param state
	 */

	public Complaint(long authorId, long subjectId, ComplaintState state, String description) {
		this.authorId = authorId;
		this.subjectId = subjectId;
		this.state = state;
		this.description = description;
	}


	public long getAuthor() {
		return authorId;
	}

	public void setAuthor(Long authorId) {
		if (authorId == Long.valueOf(0)) {
			throw new IllegalArgumentException("Complain must have an author!");
		} else
			this.authorId = authorId;
	}

	public long getSubject() {
		return subjectId;
	}

	public void setSubject(Long subjectId) {
		if (subjectId == Long.valueOf(0)) {
			throw new IllegalArgumentException("Complain must have a subject!");
		} else
			this.subjectId = subjectId;
	}

	public ComplaintState getState() {
		return state;
	}

	public void setState(ComplaintState state) {
		if (state == null) {
			throw new IllegalArgumentException("Complain must have a state");
		} else
			this.state = state;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Complains must have a description");
		}
		this.description = description;
	}
}
