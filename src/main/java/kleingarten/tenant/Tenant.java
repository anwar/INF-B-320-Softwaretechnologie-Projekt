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

package kleingarten.tenant;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Tenant {

	@OneToOne
	public UserAccount userAccount;
	public String forename;
	public String surname;
	public String address;
	public String phonenumber;
	public String birthdate;
	public String chairmanColor;
	//Siehe Dokumentation ElementCollection, wurde auch analog im Salespoint-Produkt verwendet.
	@ElementCollection
	protected Set<Role> roles = new HashSet();
	private @Id
	@GeneratedValue
	long id;
	private Role role;

	/**
	 * Private constructor of class {@link Tenant}, which is used by the Spring Framework
	 */
	private Tenant() {
	}

	/**
	 * Constructor of class {@link Tenant}
	 *
	 * @param forename    of the tenant
	 * @param surname     both parameters are used to identify the tenant by name
	 * @param address     address of the tenant for physical mailing service as String
	 * @param phonenumber of the tenant as String
	 * @param birthdate   of the tenant as String
	 * @param userAccount userAccount of the tenant as class {@link UserAccount} given by the Salespoint-Framework
	 *                    containing and saving user data (such as password, email and username)
	 */
	public Tenant(String forename, String surname, String address, String phonenumber, String birthdate,
				  UserAccount userAccount) {
		this.forename = forename;
		this.surname = surname;
		this.address = address;
		this.phonenumber = phonenumber;
		this.userAccount = userAccount;
		this.birthdate = birthdate;
	}

	/**
	 * Getter for the address of a {@link Tenant}
	 *
	 * @return address as String
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Setter for the address of class {@link Tenant}
	 *
	 * @param address as String
	 */
	public void setAddress(String address) {
		if (address == null) {
			throw new IllegalArgumentException("Address must not be null!");
		}
		this.address = address;
	}

	/**
	 * Getter for the forename of a {@link Tenant}
	 *
	 * @return forename as String
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * Setter for the forename of class {@link Tenant}
	 *
	 * @param forename as String
	 */
	public void setForename(String forename) {
		if (forename == null) {
			throw new IllegalArgumentException("Forename must not be null!");
		}
		this.forename = forename;
	}

	/**
	 * Getter for the surname of a {@link Tenant}
	 *
	 * @return surname as String
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Setter for the surname of class {@link Tenant}
	 *
	 * @param surname as String
	 */
	public void setSurname(String surname) {
		if (surname == null) {
			throw new IllegalArgumentException("Surname must not be null!");
		}
		this.surname = surname;
	}

	/**
	 * Getter for full name of a {@link Tenant}
	 *
	 * @return full name as String
	 */
	public String getFullName() {
		return forename + " " + surname;
	}

	/**
	 * Getter for the phone number of a {@link Tenant}
	 *
	 * @return phonenumber as String
	 */
	public String getPhonenumber() {
		return phonenumber;
	}

	/**
	 * Setter for the phone number of class {@link Tenant}
	 *
	 * @param phonenumber as String
	 */
	public void setPhonenumber(String phonenumber) {
		if (phonenumber == null) {
			throw new IllegalArgumentException("Phone number must not be null!");
		}
		this.phonenumber = phonenumber;
	}

	/**
	 * Getter for the birth date of a {@link Tenant}
	 *
	 * @return birthdate as String
	 */
	public String getBirthdate() {
		return birthdate;
	}

	/**
	 * Setter for the birth date of class {@link Tenant}
	 *
	 * @param birthdate as String
	 */
	public void setBirthdate(String birthdate) {
		if (birthdate == null) {
			throw new IllegalArgumentException("Birth date must not be null!");
		}
		this.birthdate = birthdate;
	}

	/**
	 * Getter for the id of a {@link Tenant}
	 *
	 * @return id as long
	 */
	public long getId() {
		return id;
	}

	/**
	 * Getter for the userAccount of a {@link Tenant}
	 *
	 * @return userAccount as {@link UserAccount}
	 */
	public UserAccount getUserAccount() {
		return userAccount;
	}

	/**
	 * Getter for the roles of a {@link Tenant}
	 *
	 * @return roles in sorted joined String
	 */
	public String getRoles() {
		return userAccount.getRoles().toList().stream().map(n -> new TenantRole(n)).sorted().map(n -> n.toString())
				.collect(Collectors.joining(", "));
	}

	/**
	 * Getter for the Role of a {@link Tenant}
	 *
	 * @return Role as {@link Role}
	 */
	public Role getRole() {
		return userAccount.getRoles().iterator().next();
	}

	/**
	 * Getter for the email of a {@link Tenant}
	 *
	 * @return email as String
	 */
	public String getEmail() {
		return this.userAccount.getEmail();
	}

	/**
	 * Setter for the email of the userAccount of class {@link Tenant}
	 *
	 * @param email as String
	 */
	public void setEmail(String email) {
		if (email == null) {
			throw new IllegalArgumentException("Email must not be null!");
		}
		this.userAccount.setEmail(email);
	}

	/**
	 * Adds a Role of a userAccount of class {@link Tenant}
	 *
	 * @param role as class {@link Role}
	 */
	public void addRole(Role role) {
		this.userAccount.add(role);
	}

	/**
	 * Removes a Role of a userAccount of class {@link Tenant}
	 *
	 * @param role as class {@link Role}
	 */
	public void removeRole(Role role) {
		this.userAccount.remove(role);
	}

	/**
	 * Deletes {@link Role} of a {@link Tenant}
	 */
	public void deleteRoles() {
		for (Role r : userAccount.getRoles().toList()) {

			userAccount.remove(r);
		}
	}

	/**
	 * Method to check if a {@link Tenant} has a specific {@link Role}
	 *
	 * @param role {@link Role} to be checked as {@link String}
	 * @return {@link Boolean} true if {@link Tenant} has {@link Role}, {@link Boolean} false if not
	 */
	public boolean hasRole(String role) {
		return userAccount.hasRole(Role.of(role));
	}

	/**
	 * Getter for the color of a {@link Tenant} with the {@link Role} "Obmann"
	 *
	 * @return color as String
	 */
	public String getChairmanColor() {
		return chairmanColor;
	}

	/**
	 * Setter for the chairmanColor of a {@link Tenant} with the {@link Role} "Obmann"
	 *
	 * @param chairmanColor as String
	 */
	public void setChairmanColor(String chairmanColor) {
		this.chairmanColor = chairmanColor;
	}
}
