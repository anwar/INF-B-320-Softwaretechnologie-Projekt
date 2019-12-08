package kleingarten.complains;


// WIP we still have to do this, but if someone got time, they can start working on this

import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;

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
	//public ProductIdentifier authorPlotId;
	//public ProductIdentifier subjectPlotId;
	public ComplainsState state;
	public String description;

	/**
	 * Private constructor of class {@link Complains}, which is used by the Spring Framework
	 */
	private Complains(){
		super();
	}

	/**
	 *
	 *
	 *
	 * @param state
	 */

	public Complains(long authorId, long subjectId /*, ProductIdentifier authorPlotId, ProductIdentifier subjectPlotId, */,ComplainsState state, String description){
		this.authorId = authorId;
		this.subjectId = subjectId;
		//this.authorPlotId = authorPlotId;
		//this.subjectPlotId = subjectPlotId;
		this.state = state;
		this.description = description;
	}


	public long getAuthor() {
		return authorId;
	}
/*
	public String getAuthorName(){
		return authorId.getForename() + " " + authorId.getSurname();
	}*/

	public long getSubject() {
		return subjectId;
	}
/*
	public String getSubjectName(){
		return subject.getForename() + " " + subject.getSurname();
	}*/

	/*public ProductIdentifier getAuthorPlot() {
		return authorPlotId;
	}

	public ProductIdentifier getSubjectPlot() {
		return subjectPlotId;
	}*/

	public ComplainsState getState() {
		return state;
	}

	/*public void setAuthor(Tenant author) {
		if(author == null){
			throw new IllegalArgumentException("Complain must have an author!");
		} else
			this.author = author;
	}

	public void setSubject(Tenant subject) {
		if(subject == null){
			throw new IllegalArgumentException("Complain must have a subject!");
		} else
			this.subject = subject;
	} */

	/*public void setAuthorPlot(Plot authorPlot) {
		if(authorPlot == null){
			throw new IllegalArgumentException("Plot must exist!");
		} else
			this.authorPlot = authorPlot;
	}

	public void setSubjectPlot(Plot subjectPlot) {
		if(subjectPlot == null){
			throw new IllegalArgumentException("Plot must exist!");
		} else
			this.subjectPlot = subjectPlot;
	}*/

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
}
