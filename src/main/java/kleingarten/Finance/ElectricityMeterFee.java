package kleingarten.Finance;

public class ElectricityMeterFee extends Fee {

	public ElectricityMeterFee(){
		this.title = "Grundmiete für Stromzähler";
		this.text = "";
		this.count = 1;
	}
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.ELECTRICITYMETER_DEFAULT;
	}
}
