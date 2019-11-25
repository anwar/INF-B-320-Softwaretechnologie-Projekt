package kleingarten.Finance;

import java.util.HashMap;
import java.util.Map;

public class WaterFee extends Fee {
	
	@Override
		public double getDefaultPrice(int year) {
			// TODO Auto-generated method stub
			return DefaultVaulues.WATER_DEFAULT;
		}
	
}
