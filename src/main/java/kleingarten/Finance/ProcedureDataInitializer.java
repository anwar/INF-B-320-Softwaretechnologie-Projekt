package kleingarten.Finance;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.core.SalespointIdentifier;
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

		Procedure p1 = procedureManager.add(new Procedure( 2018, "testID", 186.52d, 1l ));
		Procedure p2 = procedureManager.add(new Procedure( 2018, "testIDPlot2", 123.52d, 1l ));
		Procedure p3 = procedureManager.add(new Procedure( 2018, "23", 123.52d, 2l ));
		p1.setWatercount(500);
		p2.setWatercount(400);
		p3.setWatercount(300);
		p3.addSubTenant(1l);

		Procedure procedure = procedureManager.getProcedure(2018, new SalespointIdentifier("testID"));
		
		LOG.info("Gefunden(2018): "+ (procedure==null ? "nix gefunden" : procedure.getWatercount()) );
		
		
		Procedure procedure2 = procedureManager.getProcedure(2017, new SalespointIdentifier("testID"));
		
		LOG.info("Gefunden(2017): "+ (procedure2==null ? "nix gefunden" : procedure2.getWatercount()) );
		
		Procedure proc = procedureManager.getProcedure(2018, new SalespointIdentifier("testID"));
		
		LOG.info(proc==null ? "nix gefunden" : proc.getWatercount()+"" );
		
		for(Procedure pro:procedureManager.getProcedures(2018, 1l)) {

			
			//LOG.info(pro==null ? "nix gefunden" : pro.getWatercount()+"" );
		}
		
		for(Procedure pro:procedureManager.getAll(1l)) {

			
			//LOG.info(pro==null ? "nix gefunden" : pro.getWatercount()+"" );
		}
		
		
		
//
//		//Fee water = new WaterFee(p1.getWatercount());
//
//		LOG.info("Wasserstand: "+procedureManager.get(p1.getId()).getWatercount());

	}

}
