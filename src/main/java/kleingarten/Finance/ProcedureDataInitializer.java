package kleingarten.Finance;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.core.SalespointIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotCatalog;
import kleingarten.plot.PlotService;
import kleingarten.tenant.TenantManager;

@Component
@Order(100)
public class ProcedureDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(ProcedureDataInitializer.class);
	private final ProcedureManager procedureManager;
	private final PlotCatalog plotService;
	private final TenantManager tenantManager;

	ProcedureDataInitializer(ProcedureManager procedureManager, PlotCatalog plotService, TenantManager tenantManager) {
		this.procedureManager = procedureManager;
		this.plotService = plotService;
		this.tenantManager = tenantManager;
	}

	@Override
	public void initialize() {
		
		Plot plot1 = plotService.findByName("1").toList().get(0);

		Procedure p1 = procedureManager.add(new Procedure( 2018, plot1, 1l ));
		Procedure p2 = procedureManager.add(new Procedure( 2018, "testIDPlot2", 123.52d, 1l ));
		Procedure p3 = procedureManager.add(new Procedure( 2018, "23", 123.52d, 2l ));
		Procedure p4 = procedureManager.add(new Procedure( 2018, "11", 121.52d, 3l ));
		p1.setWatercount(500);
		p2.setWatercount(400);
		p3.setWatercount(300);
		p4.setPowercount(600);
		p3.addSubTenant(1l);
		
		

		Procedure procedure = procedureManager.getProcedure(2018, plot1);
//
		LOG.info("Gefunden(2018): "+ (procedure==null ? "nix gefunden" : procedure.getWatercount()) );
//
//		Procedure procedure2 = procedureManager.getProcedure(2017, new SalespointIdentifier("testID"));
//
//		LOG.info("Gefunden(2017): "+ (procedure2==null ? "nix gefunden" : procedure2.getWatercount()) );
//
//		Procedure proc = procedureManager.getProcedure(2018, new SalespointIdentifier("testID"));
//
//		LOG.info(proc==null ? "nix gefunden" : proc.getWatercount()+"" );
//
//		Procedure procedure3 = procedureManager.getProcedure(2018, new SalespointIdentifier("11"));

		//LOG.info("Gefunden(2018): "+ (procedure3==null ? "nix gefunden" : procedure3.getPowercount()) );

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
