// Copyright 2019-2020 the original author or authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package kleingarten.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Application {

	@Id
	@GeneratedValue
	private long id;
	private String firstName, lastName, email;
	private String plotId;
	//date

	private ApplicationState state;

	public Application() {
		state = ApplicationState.NEW;
	}

	public Application(String firstName, String lastName, String email, String plotId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.plotId = plotId;

		state = ApplicationState.NEW;
	}

	public void accept() {
		if (state == ApplicationState.NEW) {
			state = ApplicationState.ACCEPTED;
		}
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPlotId() {
		return plotId;
	}

	public ApplicationState getState() {
		return state;
	}

	public String getStateAsString() {
		String str;
		switch (state) {
			case NEW:
				str = "neu";
			case ACCEPTED:
				str = "angenommen";
			case DENIED:
				str = "abgelehnt";
			case HIDDEN:
				str = "hidden";
			default:
				str = "fehler";
		}
		return str;
	}

	public void deny() {
		if (state == ApplicationState.NEW || state == ApplicationState.ACCEPTED) {
			state = ApplicationState.DENIED;
		}
	}

	void hide() {
		state = ApplicationState.HIDDEN;
	}

	public String toString() {
		return "Um " + plotId + " - " + firstName + " " + lastName + " - " + email;
	}


}
