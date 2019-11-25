package kleingarten.Finance;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class FeeDataInitializer implements DataInitializer {


	private static final Logger LOG = LoggerFactory.getLogger(FeeDataInitializer.class);
	
	@Override
	public void initialize() {
		
		
		Fee f1 = new WaterFee(), //
				f2 = new PowerFee();

		LOG.info("PREIS TEST01:   "+f1.getDefaultPrice(2018));
		LOG.info("PREIS TEST02:   "+f2.getDefaultPrice(2018));
		
	}

}
