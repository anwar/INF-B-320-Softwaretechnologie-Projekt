package kleingarten.plot;

import jdk.jshell.spi.ExecutionControl;
import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import java.time.Year;

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
	 * Updates the information about a {@link Plot}
	 * @param associatedPlot {@link Plot} that should to be changed
	 * @param description new description of {@link Plot} as String
	 * @param estimator new estimator of {@link Plot} of type {@link MonetaryAmount}
	 */
	public void updatePlotInfo(Plot associatedPlot, String description, MonetaryAmount estimator) throws ExecutionControl.NotImplementedException {
		throw new ExecutionControl.NotImplementedException("Not implemented jet!");
	}

	/**
	 * Getter for all {@link Plot}s which are not rented
	 * @return free {@link Plot}s as {@link Streamable} of type {@link Plot}
	 */
	public Streamable<Plot> getFreePlots() throws ExecutionControl.NotImplementedException {
		throw new ExecutionControl.NotImplementedException("Not implemented jet!");
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
