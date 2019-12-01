package kleingarten.Finance;
import org.hibernate.validator.constraints.Range;
import org.salespointframework.core.SalespointIdentifier;

import kleingarten.plot.Plot;

public class editPowercount {
	private int year;
	private SalespointIdentifier plotId;
	private double powercount;

	public int getYear(){
		return year;
	}

	@Range(min = 0, message = "powercount can not be lower than 0")
	public double getPowercount() {
		return powercount;
	}

	public void setPowercount(double powercount) {
		this.powercount = powercount;
	}

}
