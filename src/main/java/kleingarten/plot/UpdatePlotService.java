package kleingarten.plot;

import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;

@Component
public class UpdatePlotService {
	private final PlotCatalog plotCatalog;
	private final DataService dataService;

	UpdatePlotService(PlotCatalog plotCatalog, DataService dataService){
		this.plotCatalog = plotCatalog;
		this.dataService = dataService;
	}

	/**
	 * Updates the information about a {@link Plot}
	 * @param associatedPlot {@link Plot} that should to be changed
	 * @param size new size of {@link Plot} as String
	 */
	public void updatePlotSize(Plot associatedPlot, int size) {
		if (!this.plotCatalog.existsById(associatedPlot.getId())) {
			throw new IllegalArgumentException("Plot must exist!");
		}
		associatedPlot.setSize(size);
		plotCatalog.save(associatedPlot);
		if (dataService.procedureExists(LocalDateTime.now().getYear(), associatedPlot)) {
			dataService.getProcedure(LocalDateTime.now().getYear(), associatedPlot)
					.setSize(size);
		}
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
		plotCatalog.save(associatedPlot);
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
		plotCatalog.save(associatedPlot);
	}

	/**
	 * Updates the workHours of a {@link Plot} in the associated {@link kleingarten.Finance.Procedure}
	 * @param associatedPlot {@link Plot} for which the workHours should be updated
	 * @param workHours workHours of a {@link Plot} as int
	 */
	public void updateWorkHours(Plot associatedPlot, int workHours) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * Updates the powerCount of a {@link Plot} in the associated {@link kleingarten.Finance.Procedure}
	 * @param associatedPlot {@link Plot} for which the counters should be updated
	 * @param powerCount value of the power meter of a {@link Plot} as double
	 */
	public void updatePowerCount(Plot associatedPlot, double powerCount) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * Updates the waterCount of a {@link Plot} in the associated {@link kleingarten.Finance.Procedure}
	 * @param associatedPlot {@link Plot} for which the counters should be updated
	 * @param waterCount value of the water meter of a {@link Plot} as double
	 */
	public void updateWaterCount(Plot associatedPlot, double waterCount) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * Adds {@link Tenant}s for a {@link Plot} to the associated {@link kleingarten.Finance.Procedure}
	 * @param associatedPlot {@link Plot} for which the {@link Tenant}s should be added
	 * @param tenants new {@link Tenant}s as {@link Streamable} of type {@link Tenant}
	 */
	public void addTenant(Plot associatedPlot, Streamable<Tenant> tenants) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
}
