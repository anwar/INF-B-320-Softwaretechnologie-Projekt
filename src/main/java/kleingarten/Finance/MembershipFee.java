package kleingarten.Finance;

public class MembershipFee extends Fee {

	public MembershipFee(){
		this.title = "Mitgliedsbeitrag";
		this.text = "";
		this.count = 1;
		this.defaultPrice = getDefaultPrice(2018);
	}
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.MEMBERSHIPFEE_DEFAULT;
	}
}
