package kleingarten.plot;

import kleingarten.finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;

@Component
public class UpdatePlotService {
	private final PlotCatalog plotCatalog;
	private final ProcedureManager procedureManager;

	UpdatePlotService(PlotCatalog plotCatalog, ProcedureManager procedureManager){
		this.plotCatalog = plotCatalog;
		this.procedureManager = procedureManager;
	}

	/**
	 * Updates the information about a {@link Plot}
	 * @param associatedPlot {@link Plot} that should to be changed
	 * @param description new description of {@link Plot} as String
	 */
	public void updatePlotDescription(Plot associatedPlot, String description) {
		if (!this.plotCatalog.existsById(associatedPlot.getId())) {
			throw new IllegalArgumentException("Plot must exist!");
		}
		associatedPlot.setDescription(description);
	}

	/**
	 * Updates the information about a {@link Plot}
	 * @param associatedPlot {@link Plot} that should to be changed
	 * @param estimator new estimator of {@link Plot} of type {@link MonetaryAmount}
	 */
	public void updatePlotPrice(Plot associatedPlot, MonetaryAmount estimator) {
		if (!this.plotCatalog.existsById(associatedPlot.getId())) {
			throw new IllegalArgumentException("Plot must exist!");
		}
		associatedPlot.setEstimator(estimator);
	}

	/**
	 * Updates the workHours of a {@link Plot} in the associated {@link kleingarten.finance.Procedure}
	 * @param associatedPlot {@link Plot} for which the workHours should be updated
	 * @param workHours workHours of a {@link Plot} as int
	 */
	public void updateWorkHours(Plot associatedPlot, int workHours) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * Updates the powerCount of a {@link Plot} in the associated {@link kleingarten.finance.Procedure}
	 * @param associatedPlot {@link Plot} for which the counters should be updated
	 * @param powerCount value of the power meter of a {@link Plot} as double
	 */
	public void updatePowerCount(Plot associatedPlot, double powerCount) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * Updates the waterCount of a {@link Plot} in the associated {@link kleingarten.finance.Procedure}
	 * @param associatedPlot {@link Plot} for which the counters should be updated
	 * @param waterCount value of the water meter of a {@link Plot} as double
	 */
	public void updateWaterCount(Plot associatedPlot, double waterCount) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * Adds {@link Tenant}s for a {@link Plot} to the associated {@link kleingarten.finance.Procedure}
	 * @param associatedPlot {@link Plot} for which the {@link Tenant}s should be added
	 * @param tenants new {@link Tenant}s as {@link Streamable} of type {@link Tenant}
	 */
	public void addTenant(Plot associatedPlot, Streamable<Tenant> tenants) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
}
