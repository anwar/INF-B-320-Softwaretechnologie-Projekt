/*package kleingarten.complains;


// WIP we still have to do this, but if someone got time, they can start working on this

import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Class to specify {@link Complains}
 */

/*
@Entity
public class Complains {

	private @Id @GeneratedValue long id;
	//public Tenant author;
	//public Tenant subject;
	//public Plot authorPlot;
	//public Plot subjectPlot;
	public ComplainsState state;

	/**
	 * Private constructor of class {@link Complains}, which is used by the Spring Framework
	 */
/*
	private Complains(){}

	/**
	 *
	 *
	 * @param authorPlot
	 * @param subjectPlot
	 * @param state
	 */
/*
	public Complains(/*Tenant author, Tenant subject, Plot authorPlot, Plot subjectPlot, ComplainsState state){
		//this.author = author;
		//this.subject = subject;
		//this.authorPlot = authorPlot;
		//this.subjectPlot = subjectPlot;
		this.state = state;
	}


	/*public Tenant getAuthor() {
		return author;
	}

	public String getAuthorName(){
		return author.getForename() + " " + author.getSurname();
	}

	public Tenant getSubject() {
		return subject;
	}

	public String getSubjectName(){
		return subject.getForename() + " " + subject.getSurname();
	} */

	/*public Plot getAuthorPlot() {
		return authorPlot;
	}

	public Plot getSubjectPlot() {
		return subjectPlot;
	}

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
	}

	public void setState(ComplainsState state) {
		if(state == null){
			throw new IllegalArgumentException("Complain must have a state");
		} else
			this.state = state;
	}
}*/
