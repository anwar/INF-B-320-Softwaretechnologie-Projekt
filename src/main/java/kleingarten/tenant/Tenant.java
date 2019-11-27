package kleingarten.tenant;

import org.h2.engine.User;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.util.Streamable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public String getAddress(){
		return address;
	}

	public String getForename(){
		return forename;
	}

	public String getSurname(){
		return surname;
	}

	public String getPhonenumber(){
		return phonenumber;
	}

	public String getBirthdate(){
		return birthdate;
	}

	public long getId(){
		return id;
	}

	public UserAccount getUserAccount(){
		return userAccount;
	}

	public Streamable<Role> getRoles(){
		return userAccount.getRoles();
	}

	public Role getRole(){
		return userAccount.getRoles().toList().get(0);
	}

	public String getEmail(){
		return this.userAccount.getEmail();
	}

	public void setForename(String forename){
		this.forename = forename;
	}

	public void setSurname(String surname){
		this.surname = surname;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public void setEmail(String email){
		this.userAccount.setEmail(email);
	}

	public void setPhonenumber(String phonenumber){
		this.phonenumber = phonenumber;
	}

	public void setBirthdate(String birthdate){
		this.birthdate = birthdate;
	}

	public void addRole(Role role){
		this.userAccount.add(role);
	}
	public void removeRole(Role role){
		this.userAccount.remove(role);
	}

	public void setUserAccount(UserAccount userAccount){
		this.userAccount = userAccount;
	}

	private Tenant(){}

	public Tenant(String forename, String surname, String address, String email, String phonenumber, String birthdate, UserAccount userAccount/*, Role role*/){
		this.forename = forename;
		this.surname = surname;
		this.address = address;
		this.phonenumber = phonenumber;
		this.userAccount = userAccount;
		this.birthdate = birthdate;
		this.userAccount.setEmail(email);

	}

}
