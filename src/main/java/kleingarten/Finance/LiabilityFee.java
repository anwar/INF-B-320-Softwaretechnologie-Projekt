package kleingarten.Finance;

public class LiabilityFee extends Fee {

	public LiabilityFee(){
		this.title = "Haftpflichtbeitrag";
		this.text = "";
		this.count = 1;
	}
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.LIABILITYFEE_DEFAULT;
	}
}
