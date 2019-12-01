package kleingarten.plot;

import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.Finance.Procedure;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

@Component
public class PlotService {
	private final PlotCatalog plotCatalog;
	private final ProcedureManager procedureManager;

	PlotService(PlotCatalog plotCatalog, ProcedureManager procedureManager){
		this.plotCatalog = plotCatalog;
		this.procedureManager = procedureManager;
	}

	/**
	 * Create a new object of type {@link Plot} and add it to the {@link PlotCatalog}
	 * @param name name of the {@link Plot} as String
	 * @param size size of the {@link Plot} as int
	 * @param description description of the {@link Plot} as String
	 */
	public Plot addNewPlot(String name, int size, String description) {
		if (this.existsByName(name)) {
			throw new IllegalArgumentException("Plot with the given name exists already!");
		}
		return this.plotCatalog.save(new Plot(name, size, description));
	}

	/**
	 * Check if there is a {@link Plot} with the given name in the {@link PlotCatalog}
	 * @param name name of the {@link Plot} as String
	 * @return true, if {@link Plot} with the given name exists
	 */
	public boolean existsByName(String name) {
		return !this.plotCatalog.findByName(name).isEmpty();
	}

	/**
	 * Get the associated {@link Procedure} for a {@link Plot}
	 * @param year the year for which the {@link Procedure} should be found
	 * @param plotId the Id of the {@link Plot} for which the {@link Procedure} should be found
	 * @return {@link Procedure} which is searched for
	 */
	public Procedure getProcedure(int year, ProductIdentifier plotId) {
		Procedure procedure = procedureManager.getProcedure(year,plotId);
		if (procedure == null) {
			throw new IllegalArgumentException("Procedure must not be null!");
		}
		return procedure;
	}

	/**
	 * Get all rented {@link Plot}s of a specific user
	 * @param year year for which the rented {@link Plot}s should be searched
	 * @param tenant {@link Tenant} for which the {@link Plot}s should be searched
	 * @return rented {@link Plot}s as {@link Streamable} of type {@link Plot}
	 */
	public Streamable<Plot> getAssociatedPlots(int year,  Tenant tenant) {
		throw new UnsupportedOperationException();
	}

}
