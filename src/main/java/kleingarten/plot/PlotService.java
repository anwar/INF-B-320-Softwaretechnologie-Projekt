package kleingarten.plot;

import kleingarten.Finance.ProcedureManager;
import kleingarten.Finance.Procedure;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Component;

@Component
public class PlotService {
	private final PlotCatalog plotCatalog;

	/**
	 * Constructor of the class {@link PlotService} used by Spring
	 * @param plotCatalog repository of plots as {@link PlotCatalog}
	 */
	PlotService(PlotCatalog plotCatalog){
		this.plotCatalog = plotCatalog;
	}

	/**
	 * Create a new object of type {@link Plot} and add it to the {@link PlotCatalog}
	 * @param name name of the {@link Plot} as String
	 * @param size size of the {@link Plot} as int
	 * @param description description of the {@link Plot} as String
	 * @return {@link Plot} which is added
	 */
	public Plot addNewPlot(String name, int size, String description) {
		if (this.existsByName(name)) {
			throw new IllegalArgumentException("Plot with the given name exists already!");
		}
		return plotCatalog.save(new Plot(name, size, description));
	}

	/**
	 * Check if there is a {@link Plot} with the given name in the {@link PlotCatalog}
	 * @param name name of the {@link Plot} as String
	 * @return true, if {@link Plot} with the given name exists
	 */
	public boolean existsByName(String name) {
		return !plotCatalog.findByName(name).isEmpty();
	}

	/**
	 * Get the {@link Plot} object for a given Id of a {@link Plot} as {@link ProductIdentifier}
	 * @param plotId the id of the {@link Plot} as {@link ProductIdentifier}
	 * @return associated {@link Plot}
	 */
	public Plot findById(ProductIdentifier plotId) {
		if (!plotCatalog.existsById(plotId)) {
			throw new IllegalArgumentException("Plot does not exist!");
		}
		return plotCatalog.findById(plotId).get();
	}
}
