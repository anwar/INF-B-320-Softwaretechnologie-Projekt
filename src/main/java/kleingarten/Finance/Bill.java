package kleingarten.Finance;

public abstract class Bill {

	public String generateString(Procedure procedure) {

		Fee water = new WaterFee();
		water.setCount(procedure.getWatercount());

		return water.getTitle() + water.getText();
	}

	/*
	*billID shows plot Nr. and created year
	 */
	String billID;
	double totalFee;
	String description; // not necessary

	public Bill(String billID, double totalFee, String description){
		this.billID = billID;


	}
}
