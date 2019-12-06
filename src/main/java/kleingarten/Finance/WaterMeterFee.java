package kleingarten.Finance;

public class WaterMeterFee extends Fee {

	public WaterMeterFee(){
		this.title = "Grundmiete für Wasseruhr";
		this.text = "";
		this.count = 1;
		this.defaultPrice = getDefaultPrice(2018);
	}
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.WATERMETER_DEFAULT;
	}
}
