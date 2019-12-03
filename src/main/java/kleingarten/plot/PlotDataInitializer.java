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
		if (this.plotService.existsByName("1")) {
			return;
		}

		LOG.info("Creating default plots");
		Plot Plot = this.plotService.addNewPlot("1", 300, "Kleine Parzelle mit angelegtem Teich.");
		LOG.info(Plot.getId().toString());
		Plot Plot_2 = this.plotService.addNewPlot("2", 500, "Sehr große Parzelle.");
		LOG.info(Plot_2.getId().toString());
		Plot Plot_3 = this.plotService.addNewPlot("3", 120, "Kleine Parzelle.");
		LOG.info(Plot_3.getId().toString());
		Plot Plot_4 = this.plotService.addNewPlot("4", 1500, "Sehr, sehr große Parzelle.");
		LOG.info(Plot_4.getId().toString());
		Plot Plot_5 = this.plotService.addNewPlot("5", 350, "Parzelle mit angelegtem Teich.");
		LOG.info(Plot_5.getId().toString());
		Plot Plot_6 = this.plotService.addNewPlot("6", 200, "Große Parzelle.");
		LOG.info(Plot_6.getId().toString());
		this.catalog.saveAll(List.of(Plot, Plot_2, Plot_3, Plot_4, Plot_5, Plot_6));

		LOG.info("fertig");

	}
}

