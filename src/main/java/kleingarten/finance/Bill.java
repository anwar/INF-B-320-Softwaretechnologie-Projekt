package kleingarten.finance;

import java.util.List;


public class Bill {

	List<Fee> feeList;

	public Bill(Procedure procedure){
		Fee membershipFee = new Fee("Mitgliedsbeitrag",1,17.25);
		feeList.add(membershipFee);
		Fee liabilityFee = new Fee("Haftpflichtbeitrag",1,0.35);
		feeList.add(liabilityFee);
		Fee winterServiceFee = new Fee("Winterdienst",1,3);
		feeList.add(winterServiceFee);
		Fee socialContribution = new Fee("Sozialbeitrag",1,0.5);
		feeList.add(membershipFee);
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

		Fee waterFee = new Fee("Wasserkosten", procedure.getWatercount(), 1.95);
		feeList.add(waterFee);
		Fee powerFee = new Fee("Stromkosten", procedure.getPowercount(), 0.2);
		feeList.add(powerFee);
		Fee rent = new Fee("Miete", procedure.getSize(), 0.18);
		feeList.add(rent);
		Fee penalty = new Fee("Strafgeld", procedure.getWorkMinutes(), 8);
		feeList.add(penalty);
	}
}
