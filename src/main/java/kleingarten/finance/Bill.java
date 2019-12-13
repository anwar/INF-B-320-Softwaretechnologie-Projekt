package kleingarten.finance;

import java.util.List;


public class Bill {

	List<Fee> feeList;

	
	/**
	 * Create a Bill with two given Procedures.
	 * 
	 * @param mainProcedure The procedure of the current year.
	 * @param oldProcedure The procedure of the year before. !! [to SangHyun] Decide if oldProcedure can be null or not. if not, write another constructor with just the mainProcedure.
	 */
	public Bill(Procedure mainProcedure, Procedure oldProcedure){
		
		Fee membershipFee = new Fee("Mitgliedsbeitrag",1,17.25);
		feeList.add(membershipFee);
		Fee liabilityFee = new Fee("Haftpflichtbeitrag",1,0.35);
		feeList.add(liabilityFee);
		Fee winterServiceFee = new Fee("Winterdienst",1,3);
		feeList.add(winterServiceFee);
		Fee socialContribution = new Fee("Sozialbeitrag",1,0.5);
		feeList.add(socialContribution);
		Fee legalProtectionFee = new Fee("Rechtsschutz",1,0.75);
		feeList.add(legalProtectionFee);
		Fee expensesFlat = new Fee("Aufwandspauschale",1,12);
		feeList.add(expensesFlat);
		Fee rest = new Fee("Sonstige Auslagen",1,1);
		feeList.add(rest);
		Fee waterMeterFee = new Fee("Grundmiete für Wasseruhr",1,2.6);
		feeList.add(waterMeterFee);
		Fee electricityMeterFee = new Fee("Grundmiete für Stromzähler",1,1.55);
		feeList.add(electricityMeterFee);

		// This is the example. do this for all fee item to get the correct value for it
		Fee waterFee = new Fee("Wasserkosten", mainProcedure.getWatercount() - oldProcedure.getWatercount(), 1.95);
		feeList.add(waterFee);
		
		Fee powerFee = new Fee("Stromkosten", mainProcedure.getPowercount(), 0.2);
		feeList.add(powerFee);
		Fee rent = new Fee("Miete", mainProcedure.getSize(), 0.18);
		feeList.add(rent);
		Fee penalty = new Fee("Strafgeld", mainProcedure.getWorkMinutes(), 8); // you should pay less the more minutes you work!!!!
		feeList.add(penalty);
		
	}
}
