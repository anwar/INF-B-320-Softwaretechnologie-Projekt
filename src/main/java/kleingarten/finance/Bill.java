package kleingarten.finance;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;


public class Bill {

	List<Fee> feeList;


	/**
	 * Create a Bill with two given Procedures
	 *
	 * @param mainProcedure The procedure of the current year.
	 * @param oldProcedure  The procedure of the year before.
	 */
	public Bill(Procedure mainProcedure, Procedure oldProcedure) {

		Assert.notNull(mainProcedure, "Main Procedure can not be null!");

		feeList = new ArrayList<Fee>();

		initFixedCost();

		initDependentCost(mainProcedure, oldProcedure);

	}

	/**
	 * Calculate all prices in List<Fee>
	 *
	 * @param feeList list of Fees 
	 * @return sum as double
	 */
	public static double getSum(List<Fee> feeList) {
		double sum = 0d;

		for (Fee fee : feeList) {
			sum += fee.getPrice();
		}

		return sum;
	}

	/**
	 * Fees, which have a fixed number/amount and base price, will be saved in a List, feeList
	 *
	 */
	private void initFixedCost() {

		Fee membershipFee = new Fee("Mitgliedsbeitrag", 1, 17.25);
		feeList.add(membershipFee);
		Fee liabilityFee = new Fee("Haftpflichtbeitrag", 1, 0.35);
		feeList.add(liabilityFee);
		Fee winterServiceFee = new Fee("Winterdienst", 1, 3);
		feeList.add(winterServiceFee);
		Fee socialContribution = new Fee("Sozialbeitrag", 1, 0.5);
		feeList.add(socialContribution);
		Fee legalProtectionFee = new Fee("Rechtsschutz", 1, 0.75);
		feeList.add(legalProtectionFee);
		Fee expensesFlat = new Fee("Aufwandspauschale", 1, 12);
		feeList.add(expensesFlat);
		Fee rest = new Fee("Sonstige Auslagen", 1, 1);
		feeList.add(rest);
		Fee waterMeterFee = new Fee("Grundmiete für Wasseruhr", 1, 2.6);
		feeList.add(waterMeterFee);
		Fee electricityMeterFee = new Fee("Grundmiete für Stromzähler", 1, 1.55);
		feeList.add(electricityMeterFee);

	}

	/**
	 * Fees, which need to receive numbers/amounts from a procedure, will be saved in a List, feeList
	 *
	 * @param mainProcedure as {@link Procedure}
	 */
	private void initDependentCost(Procedure mainProcedure) {
		Fee waterFee = new Fee("Wasserkosten",
				mainProcedure.getWatercount(),
				1.95);
		feeList.add(waterFee);

		Fee powerFee = new Fee("Stromkosten",
				mainProcedure.getPowercount(),
				0.2);
		feeList.add(powerFee);

		Fee rent = new Fee("Miete", mainProcedure.getSize(), 0.18);
		feeList.add(rent);

		Fee penalty = new Fee("Strafgeld",
				Math.round(Math.max(0, (double) (240 - mainProcedure.getWorkMinutes()) / 60)),
				8);
		feeList.add(penalty);

	}

	/**
	 * Fees, which need to receive numbers/amounts from a procedure, will be saved in a List, feeList
	 *
	 * @param mainProcedure as {@link Procedure}
	 * @param oldProcedure  as {@link Procedure}
	 */
	private void initDependentCost(Procedure mainProcedure, Procedure oldProcedure) {
		if (oldProcedure == null) {
			initDependentCost(mainProcedure);
			return;
		}

		Fee waterFee = new Fee("Wasserkosten",
				mainProcedure.getWatercount() - oldProcedure.getWatercount(),
				1.95);
		feeList.add(waterFee);

		Fee powerFee = new Fee("Stromkosten",
				mainProcedure.getPowercount() - oldProcedure.getPowercount(),
				0.2);
		feeList.add(powerFee);

		Fee rent = new Fee("Miete",
				mainProcedure.getSize(),
				0.18);
		feeList.add(rent);

		Fee penalty = new Fee("Strafgeld",
				Math.round(Math.max(0, (double) (240 - mainProcedure.getWorkMinutes()) / 60)),
				8);
		feeList.add(penalty);
	}

}
