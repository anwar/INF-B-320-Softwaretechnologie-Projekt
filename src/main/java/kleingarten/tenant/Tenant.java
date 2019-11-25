package kleingarten.tenant;

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
//	public Streamable<Role> roles;
	public String email;       
	
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

	public String getEmail(){
		return email;
	}

	public void setForename(){
		this.forename = forename;
	}

	public void setSurname(){
		this.surname = surname;
	}

	public void setAddress(){
		this.address = address;
	}

	public void setEmail(){
		this.email = email;
	}

	public void setPhonenumber(){
		this.phonenumber = phonenumber;
	}

	public void setBirthdate(){
		this.birthdate = birthdate;
	}

	public void setRole(){
		this.roles = roles;
	}

	public void setUserAccount(){
		this.userAccount = userAccount;
	}

	private Tenant(){}

	public Tenant(String forename, String surname, String address, String email, String phonenumber, String birthdate, UserAccount userAccount, Streamable<Role> roles){
		this.forename = forename;
		this.surname = surname;
		this.address = address;
		this.email = email;
		this.phonenumber = phonenumber;
		this.userAccount = userAccount;
		this.birthdate = birthdate;
		this.roles.addAll(roles.toSet());

	}

}
