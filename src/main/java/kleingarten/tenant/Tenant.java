package kleingarten.tenant;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import java.util.HashSet;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Tenant {

	private @Id @GeneratedValue long id;
	@OneToOne public UserAccount userAccount;

	public String forename;
	public String surname;
	public String address;
	public String phonenumber;
	public String birthdate;

	//Siehe Dokumentation ElementCollection, wurde auch analog im Salespoint-Produkt verwendet.
	@ElementCollection
    protected Set<Role> roles = new HashSet();
	private Role role;

	/**
	 * Getter for the address of a {@link Tenant}
	 * @return address as String
	 */
	public String getAddress(){
		return address;
	}

	/**
	 * Getter for the forename of a {@link Tenant}
	 * @return forename as String
	 */
	public String getForename(){
		return forename;
	}

	/**
	 * Getter for the surname of a {@link Tenant}
	 * @return surname as String
	 */
	public String getSurname(){
		return surname;
	}

	/**
	 * Getter for the phone number of a {@link Tenant}
	 * @return phonenumber as String
	 */
	public String getPhonenumber(){
		return phonenumber;
	}

	/**
	 * Getter for the birth date of a {@link Tenant}
	 * @return birthdate as String
	 */
	public String getBirthdate(){
		return birthdate;
	}

	/**
	 * Getter for the id of a {@link Tenant}
	 * @return id as long
	 */
	public long getId(){
		return id;
	}

	/**
	 * Getter for the userAccount of a {@link Tenant}
	 * @return userAccount as {@link UserAccount}
	 */
	public UserAccount getUserAccount(){
		return userAccount;
	}

	/**
	 * Getter for the roles of a {@link Tenant}
	 * @return roles in sorted joined String
	 */
	public String getRoles(){
		return userAccount.getRoles().toList().stream().map(n -> new TenantRole(n)).sorted().map(n -> n.toString()).collect(Collectors.joining(", "));
	}

	/**
	 * Getter for the Role of a {@link Tenant}
	 * @return Role as {@link Role}
	 */
	public Role getRole(){
		return userAccount.getRoles().iterator().next();
	}

	/**
	 * Getter for the email of a {@link Tenant}
	 * @return email as String
	 */
	public String getEmail(){
		return this.userAccount.getEmail();
	}

	/**
	 * Setter for the forename of class {@link Tenant}
	 * @param forename as String
	 */
	public void setForename(String forename){
		if (forename == null){
			throw new IllegalArgumentException("Forename must not be null!");
		}
		this.forename = forename;
	}

	/**
	 * Setter for the surname of class {@link Tenant}
	 * @param surname as String
	 */
	public void setSurname(String surname){
		if (surname == null){
			throw new IllegalArgumentException("Surname must not be null!");
		}
		this.surname = surname;
	}

	/**
	 * Setter for the address of class {@link Tenant}
	 * @param address as String
	 */
	public void setAddress(String address){
		if (address == null){
			throw new IllegalArgumentException("Address must not be null!");
		}
		this.address = address;
	}

	/**
	 * Setter for the email of the userAccount of class {@link Tenant}
	 * @param email as String
	 */
	public void setEmail(String email){
		if (email == null){
			throw new IllegalArgumentException("Email must not be null!");
		}
		this.userAccount.setEmail(email);
	}

	/**
	 * Setter for the phone number of class {@link Tenant}
	 * @param phonenumber as String
	 */
	public void setPhonenumber(String phonenumber){
		if (phonenumber == null){
			throw new IllegalArgumentException("Phone number must not be null!");
		}
		this.phonenumber = phonenumber;
	}

	/**
	 * Setter for the birth date of class {@link Tenant}
	 * @param birthdate as String
	 */
	public void setBirthdate(String birthdate){
		if (birthdate == null){
			throw new IllegalArgumentException("Birth date must not be null!");
		}
		this.birthdate = birthdate;
	}

	/**
	 * Adds a Role of a userAccount of class {@link Tenant}
	 * @param role as class {@link Role}
	 */
	public void addRole(Role role){
		this.role = role;
		this.userAccount.add(role);
	}

	/**
	 * Removes a Role of a userAccount of class {@link Tenant}
	 * @param role as class {@link Role}
	 */
	public void removeRole(Role role){
		this.role = role;
		this.userAccount.remove(role);
	}

	/**
	 * Setter for the userAccount of class {@link Tenant}
	 * @param userAccount as class {@link UserAccount}
	 */
	public void setUserAccount(UserAccount userAccount){
		if (userAccount == null){
			throw new IllegalArgumentException("UserAccount must not be null!");
		}
		this.userAccount = userAccount;
	}


	/**
	 * Private constructor of class {@link Tenant}, which is used by the Spring Framework
	 */
	private Tenant(){}

	/**
	 * Constructor of class {@link Tenant}
	 * @param forename
	 * @param surname both parameters are used to identify the tenant by name
	 * @param address address of the tenant for physical mailing service as String
	 * @param phonenumber phone number of the tenant as String
	 * @param birthdate birth date of the tenant as String
	 * @param userAccount userAccount of the tenant as class {@link UserAccount} given by the Salespoint-Framework containing and saving
	 *                    user data (such as password, email and username)
	 */
	public Tenant(String forename, String surname, String address, String phonenumber, String birthdate, UserAccount userAccount){
		this.forename = forename;
		this.surname = surname;
		this.address = address;
		this.phonenumber = phonenumber;
		this.userAccount = userAccount;
		this.birthdate = birthdate;
	}

}
