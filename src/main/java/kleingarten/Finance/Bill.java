package kleingarten.Finance;

public abstract class Bill {

	public String generateString(Procedure procedure) {
		
		Fee water = new WaterFee();
		water.setCount(procedure.getWatercount());
		
		return water.getTitle() + water.getText();
	}
	
}
