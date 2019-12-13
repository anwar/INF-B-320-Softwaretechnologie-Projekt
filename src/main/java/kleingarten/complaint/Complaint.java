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

	@SuppressWarnings("unused")
	private Complaint() {
		this.plot = null;
		this.complainant = null;
		this.subject = null;
		this.description = null;
		this.state = null;
	}

	public Plot getPlot() {
		return plot;
	}

	public Tenant getComplainant() {
		return complainant;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		Assert.hasText(subject, "subject must not be null!");
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		Assert.hasText(description, "description must not be null!");
		this.description = description;
	}

	public ComplaintState getState() {
		return state;
	}

	public void setState(ComplaintState state) {
		this.state = state;
	}

	public Tenant getAssignedObmann() {
		return assignedObmann;
	}

	public void setAssignedObmann(Tenant assignedObmann) {
		Assert.notNull(assignedObmann, "assignedObmann must not be null!");
		this.assignedObmann = assignedObmann;
	}
}
