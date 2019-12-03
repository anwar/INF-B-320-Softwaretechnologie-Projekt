package kleingarten.Finance;

public class SocialContribution extends Fee {

	public SocialContribution(){
		this.title = "Sozialbeitrag";
		this.text = "";
		this.count = 1;
		this.defaultPrice = getDefaultPrice(2018);
	}
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.SOCIALCONTRIBUTION_DEFAULT;
	}
}
