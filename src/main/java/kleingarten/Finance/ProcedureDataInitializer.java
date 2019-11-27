package kleingarten.Finance;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcedureDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(ProcedureDataInitializer.class);
	private final ProcedureManager procedureManager;

	ProcedureDataInitializer(ProcedureManager procedureManager) {
		this.procedureManager = procedureManager;
	}

	@Override
	public void initialize() {

		Procedure p1 = procedureManager.add(new Procedure( 2018, 186.52d, 1l ));
		p1.setWatercount(500);




		//Fee water = new WaterFee(p1.getWatercount());

		LOG.info("Wasserstand: "+procedureManager.get(p1.getId()).getWatercount());

	}

}
