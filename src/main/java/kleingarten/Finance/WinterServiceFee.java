package kleingarten.Finance;

public class WinterServiceFee extends Fee {

	public WinterServiceFee(){
		this.title = "Winterdienst";
		this.text = "";
		this.count = 1;
		this.defaultPrice = getDefaultPrice(2018);
	}
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.WINTERSERVICEFEE_DEFAULT;
	}
}
