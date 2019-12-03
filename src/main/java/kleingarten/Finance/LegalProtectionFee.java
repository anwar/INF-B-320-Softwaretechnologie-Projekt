package kleingarten.Finance;

public class LegalProtectionFee extends Fee {

	public LegalProtectionFee(){
		this.title = "Rechtsschutz";
		this.text = "";
		this.count = 1;
		this.defaultPrice = getDefaultPrice(2018);
	}
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.LEGALPROTECTIONFEE_DEFAULT;
	}
}
