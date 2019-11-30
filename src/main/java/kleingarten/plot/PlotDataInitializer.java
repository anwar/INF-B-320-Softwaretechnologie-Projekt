package kleingarten.plot;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.core.SalespointIdentifier;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import java.util.List;

@Component
public class PlotDataInitializer implements DataInitializer{
	private static final Logger LOG = LoggerFactory.getLogger(PlotDataInitializer.class);
	private final PlotCatalog catalog;
	private final PlotService plotService;

	PlotDataInitializer(PlotCatalog catalog, PlotService plotService){
		Assert.notNull(catalog, "PlotCatalog must not be null!");
		Assert.notNull(plotService, "PlotService must not be null!");
		this.catalog = catalog;
		this.plotService = plotService;
	}

	public void initialize(){
		if (this.plotService.existsByName("23")) {
			return;
		}

		LOG.info("Creating default plots");
		var Plot = this.plotService.addNewPlot("23", 300, "Kleine Parzelle mit angelegtem Teich.");
		LOG.info(Plot.getId().toString());
		var Plot_2 = this.plotService.addNewPlot("2", 500, "Sehr große Parzelle.");
		LOG.info(Plot_2.getId().toString());
		this.catalog.saveAll(List.of(Plot, Plot_2));

		//small test, i cant get the ProductIdentifier so i had to use the name in ProcedureDataInitializer[Sascha]
		LOG.info(plotService.getProcedure(2018, "23").toString());
		
		LOG.info("fertig");

	}
}

