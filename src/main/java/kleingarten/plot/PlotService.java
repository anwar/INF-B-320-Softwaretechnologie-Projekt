package kleingarten.plot;

import com.querydsl.core.Tuple;
import jdk.jshell.spi.ExecutionControl;
import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import org.h2.engine.Procedure;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class PlotService {
	private final PlotCatalog plotCatalog;
	private final ProcedureManager procedureManager;

	PlotService(PlotCatalog plotCatalog, ProcedureManager procedureManager){

		Assert.notNull(plotCatalog, "PlotCatalog must not be null!");
		Assert.notNull(procedureManager, "ProcedureManager must not be null!");
		this.plotCatalog = plotCatalog;
		this.procedureManager = procedureManager;
	}

	/**
	 * Creates a new object of type {@link Plot} and adds it to the {@link PlotCatalog}
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
	 * Checks if there is a {@link Plot} with the given name in the {@link PlotCatalog}
	 * @param name name of the {@link Plot} as String
	 * @return true, if {@link Plot} with the given name exists
	 */
	public boolean existsByName(String name) {
		return !this.plotCatalog.findByName(name).isEmpty();
	}

	/**
	 * Getter for a {@link Plot}
	 * @param plotId ID of the {@link Plot} which should be found
	 * @return {@link Plot} which is searched
	 */
	public Plot getPlot(ProductIdentifier plotId) {
		if (!this.plotCatalog.existsById(plotId)) {
			throw  new IllegalArgumentException("Plot must exist!");
		}
		return this.plotCatalog.findById(plotId).get();
	}

	/**
	 * Gets the associated {@link Procedure} for a {@link Plot}
	 * @param year the year for which the {@link Procedure} should be found
	 * @param plotId the Id of the {@link Plot} for which the {@link Procedure} should be found
	 * @return {@link Procedure} which is searched
	 */
	public Procedure getProcedure(int year, ProductIdentifier plotId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Getter for all rented {@link Plot}s of a specific user
	 * @param year year for which the rented {@link Plot}s should be searched
	 * @param tenant {@link Tenant} for which the {@link Plot}s should be searched
	 * @return rented {@link Plot}s as {@link Streamable} of type {@link Plot}
	 * @throws ExecutionControl.NotImplementedException
	 */
	Streamable<Plot> getAssociatedPlots(Year year,  Tenant tenant) throws ExecutionControl.NotImplementedException {
		throw new ExecutionControl.NotImplementedException("Not implemented jet!");
	}

}
