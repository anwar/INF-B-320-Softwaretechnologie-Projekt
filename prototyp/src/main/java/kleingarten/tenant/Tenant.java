package kleingarten.tenant;

import org.salespointframework.useraccount.UserAccount;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import kleingarten.plotManagement.Plot;
import org.springframework.data.util.Streamable;
import org.springframework.ui.Model;

@Entity
public class Tenant{

	private @Id @GeneratedValue long id;

	@OneToOne private UserAccount userAccount;

	public String name;
	public String address;
	public String email;
	public String phonenumber;
	public String birthdate;


	public String getAddress(){
		return address;
	}

	public String getName(){
		return name;
	}

	public String getEmail(){
		return email;
	}

	public String getPhonenumber(){
		return phonenumber;
	}

	public String getBirthdate(){
		return birthdate;
	}

	public void setName(){
		this.name = name;
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

	private Tenant(){}


	public Tenant(String name, String address, String email, String phonenumber, String birthdate, UserAccount userAccount/*, Plot mainPlot*/){
		this.name = name;
		this.address = address;
		this.email = email;
		this.phonenumber = phonenumber;
		this.userAccount = userAccount;
		this.birthdate = birthdate;

	}

	public long getId(){
		return id;
	}

	public UserAccount getUserAccount(){
		return userAccount;
	}

	public void setUserAccount(){
		this.userAccount = userAccount;
	}



	public void anonymize(){
		address = "";
		phonenumber = "";
		birthdate = "";
		userAccount.setEnabled(false);
	}


}
