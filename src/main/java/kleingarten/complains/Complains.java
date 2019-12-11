package kleingarten.complains;


// WIP we still have to do this, but if someone got time, they can start working on this

import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.thymeleaf.context.ILazyContextVariable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Class to specify {@link Complains}
 */

@Entity
public class Complains {


	private @Id @GeneratedValue long id;
	public long authorId;
	public long subjectId;
	public ComplainsState state;
	public String description;


	/**
	 * Private constructor of class {@link Complains}, which is used by the Spring Framework
	 */
	private Complains(){
	}

	/**
	 *
	 *
	 *
	 * @param state
	 */

	public Complains(long authorId, long subjectId , ComplainsState state, String description){
		this.authorId = authorId;
		this.subjectId = subjectId;
		this.state = state;
		this.description = description;
	}


	public long getAuthor() {
		return authorId;
	}



	public long getSubject() {
		return subjectId;
	}


	public ComplainsState getState() {
		return state;
	}

	public void setAuthor(Long authorId) {
		if(authorId == null){
			throw new IllegalArgumentException("Complain must have an author!");
		} else
			this.authorId = authorId;
	}

	public void setSubject(Long subjectId) {
		if(subjectId  == null){
			throw new IllegalArgumentException("Complain must have a subject!");
		} else
			this.subjectId = subjectId;
	}


	public void setState(ComplainsState state) {
		if(state == null){
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
		if(description == null){
			throw new IllegalArgumentException("Complains must have a description");
		}
		this.description = description;
	}
}
