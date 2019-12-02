package kleingarten.Finance;

import java.util.HashMap;
import java.util.Map;

public class Penalty extends Fee {
/*	Procedure procedure;

	public Penalty(){
		this.title = "Pflichtstunde";
		this.text = "";
		this.count = procedure.getWorkMinutes();	// Need to work on later
	}
*/

	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.PENALTY_DEFAULT;
	}
}
