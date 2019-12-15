/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kleingarten.complaint;

import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Class to specify a {@link Complaint}.
 */
@Entity
public class Complaint {

	@Id
	@GeneratedValue
	private long id;
	@OneToOne
	private Plot plot;
	@OneToOne
	private Tenant complainant;
	private String subject;
	private String description;
	private ComplaintState state;
	@OneToOne
	private Tenant assignedObmann;

	/**
	 * Creates a new {@link Complaint}.
	 *
	 * @param plot        against which the complaint is created
	 * @param complainant that made the complaint
	 * @param subject     for the complaint
	 * @param description for the complaint
	 * @param state       of the complaint
	 */
	public Complaint(Plot plot, Tenant complainant,
					 String subject, String description,
					 ComplaintState state) {
		Assert.notNull(plot, "plot must not be null!");
		Assert.notNull(complainant, "tenant must not be null!");
		Assert.hasText(subject, "subject must not be null or empty!");
		Assert.hasText(description, "description must not be null or empty!");
		Assert.notNull(state, "state must not be null!");

		this.plot = plot;
		this.complainant = complainant;
		this.subject = subject;
		this.description = description;
		this.state = state;
		// At the time of creation, the Obmann that is responsible for the plot in question is assigned.
		this.assignedObmann = plot.getChairman();
	}

	/**
	 * Private constructor for {@link Complaint} used by Spring
	 */
	@SuppressWarnings("unused")
	private Complaint() {
		this.plot = null;
		this.complainant = null;
		this.subject = null;
		this.description = null;
		this.state = null;
	}

	/**
	 * Getter for the id of a {@link Complaint}
	 * @return id as long
	 */
	public long getId() {
		return id;
	}

	/**
	 * Getter for the {@link Plot} of a {@link Complaint}
	 * @return plot as {@link Plot}
	 */
	public Plot getPlot() {
		return plot;
	}

	/**
	 * Getter for the complainant of a {@link Complaint}
	 * @return complainant as {@link Tenant}
	 */
	public Tenant getComplainant() {
		return complainant;
	}

	/**
	 * Getter for the subject of a {@link Complaint}
	 * @return subject as {@link String}
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Setter for the subject of a {@link Complaint}
	 * @param subject as {@link String}
	 */
	public void setSubject(String subject) {
		Assert.hasText(subject, "subject must not be null!");
		this.subject = subject;
	}

	/**
	 * Getter for the description of a {@link Complaint}
	 * @return description as {@link String}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for the description of a {@link Complaint}
	 * @param description as {@link String}
	 */
	public void setDescription(String description) {
		Assert.hasText(description, "description must not be null!");
		this.description = description;
	}

	/**
	 * Getter for the state of a {@link Complaint}
	 * @return state as {@link ComplaintState}
	 */
	public ComplaintState getState() {
		return state;
	}

	/**
	 * Setter for the state of a {@link Complaint}
	 * @param state as {@link ComplaintState}
	 */
	public void setState(ComplaintState state) {
		Assert.notNull(state, "state must not be null!");
		this.state = state;
	}

	/**
	 * Getter for the assigned obmann of a {@link Complaint}
	 * @return assigned obmann as {@link Tenant}
	 */
	public Tenant getAssignedObmann() {
		return assignedObmann;
	}

	/**
	 * Setter for the assigned obmann
	 * @param assignedObmann as {@link Tenant}
	 */
	public void setAssignedObmann(Tenant assignedObmann) {
		Assert.notNull(assignedObmann, "assignedObmann must not be null!");
		this.assignedObmann = assignedObmann;
	}
}
