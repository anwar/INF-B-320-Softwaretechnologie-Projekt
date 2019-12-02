package kleingarten.Finance;

import javax.persistence.Entity;
import java.util.List;

public class Bill {

	/*
	 *billID shows plot Nr. and created year
	 */
	private String billID;
	Procedure procedure;

	public String getBillID() {
		return billID;
	}

	public void setBillID(String billID) {
		this.billID = billID;
	}

	public Bill(){}

	List<Fee> fees;
	public Bill(Procedure procedure){
		this.billID = billID;

		Fee membershipFee = new MembershipFee();
		fees.add(membershipFee);
		Fee liabilityFee = new LiabilityFee();
		fees.add(liabilityFee);
		Fee winterServiceFee = new WinterServiceFee();
		fees.add(winterServiceFee);
		Fee socialContribution = new SocialContribution();
		fees.add(socialContribution);
		Fee legalProtectionFee = new LegalProtectionFee();
		fees.add(legalProtectionFee);
		Fee expensesFlat = new ExpensesFlat();
		fees.add(expensesFlat);
		Fee waterMeterFee = new WaterMeterFee();
		fees.add(waterMeterFee);
		Fee electricityMeterFee = new ElectricityMeterFee();
		fees.add(electricityMeterFee);
		/*double water = procedure.getWatercount();
		Fee waterFee = new WaterFee();
		fees.add(waterFee);*/
	}

	/*
	public String generateString(Procedure procedure) {

		Fee water = new WaterFee();
		water.setCount(procedure.getWatercount());

		return water.getTitle() + water.getText();
	}
	 */
}



