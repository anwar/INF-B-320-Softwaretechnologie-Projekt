package kleingarten.Finance;

import java.util.HashMap;
import java.util.Map;

public class PowerFee extends Fee {
	
	@Override
	public double getDefaultPrice(int year) {
		// TODO Auto-generated method stub
		return DefaultVaulues.POWER_DEFAULT;
	}
	
}
