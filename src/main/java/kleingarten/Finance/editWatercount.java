package kleingarten.Finance;
import org.hibernate.validator.constraints.Range;
import org.salespointframework.core.SalespointIdentifier;

import kleingarten.plot.Plot;

public class editWatercount {
	private int year;
	private SalespointIdentifier plotId;
	private double watercount;

	public int getYear(){
		return year;
	}

	@Range(min = 0, message = "watercount can not be lower than 0")
	public double getWatercount() {
		return watercount;
	}

	public void setWatercount(double watercount) {
		this.watercount = watercount;
	}

}
