package kleingarten.plotManagement;

import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


@Component
public class PlotInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(PlotInitializer.class);

	private final PlotCatalog catalog;


	PlotInitializer(PlotCatalog catalog){
		Assert.notNull(catalog, "PlotCatalog must not be null!");
		this.catalog = catalog;
	}

	public void initialize(){

		LOG.info("Creating default plots");
		var Plot = new Plot(23, "A", "Kleine Parzelle mit angelegtem Teich.");
		catalog.save(Plot);
		LOG.info("fertig");

	}
}
