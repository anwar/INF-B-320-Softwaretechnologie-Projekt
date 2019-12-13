package kleingarten.plot;

import kleingarten.tenant.Tenant;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.Role;
import org.springframework.stereotype.Component;

import java.util.Set;

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

	/**
	 * Get the {@link Plot}s which are administrated by the given {@link Tenant}
	 * @param chairman {@link Tenant} with {@link Role} "Obmann" who administrates {@link Plot}s
	 * @return administrated {@link Plot}s as {@link Set}
	 */
	public Set<Plot> getPlotsFor(Tenant chairman) {
		if (!chairman.getUserAccount().hasRole(Role.of("Obmann"))) {
			throw new IllegalArgumentException("Tenant must have role 'Obmann'");
		}
		return plotCatalog.findByChairman(chairman);
	}
}
