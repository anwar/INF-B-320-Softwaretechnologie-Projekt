package kleingarten.tenant;

import javax.validation.constraints.NotEmpty;

class RegistrationForm{

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}")
	private final String name;

	@NotEmpty(message = "{RegistrationForm.address.NotEmpty}")
	private final String address;


	@NotEmpty(message = "{RegistrationForm.phonenumber.NotEmpty}")
	private final String phonenumber;

	@NotEmpty(message = "{RegistrationForm.birthday.NotEmpty}")
	private final String birthdate;

	@NotEmpty(message = "{RegistrationForm.userAccount.NotEmpty}")
	private final String userAccount;

	@NotEmpty(message = "{RegistrationForm.email.NotEmpty}")
	private final String email;

	public RegistrationForm(String name, String address, String phonenumber, String birthdate, String userAccount, String email){
		this.name = name;
		this.address = address;
		this.phonenumber = phonenumber;
		this.birthdate = birthdate;
		this.userAccount  =userAccount;
		this.email = email;
	}

	public String getName(){return name;}
	public String getAddress(){return address;}
	public String getPhonenumber(){return phonenumber;}
	public String getBirthdate(){return birthdate;}
	public String getEmail(){return email;}
	public String getUserAccount(){return userAccount;}


}
