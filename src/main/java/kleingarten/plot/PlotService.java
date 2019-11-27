package kleingarten.plot;

import jdk.jshell.spi.ExecutionControl;
import kleingarten.tenant.Tenant;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;
import java.time.Year;

@Component
public class PlotService {
	private PlotCatalog plotCatalog;

	/**
	 * Greates a new object of type {@link Plot} and adds it to the {@link PlotCatalog}
	 * @param size size of the {@link Plot} as int
	 * @param electricityCount value of the electric meter of the {@link Plot} as double
	 * @param waterCount value of the water meter of the {@link Plot} as double
	 * @param description description of the {@link Plot} as String
	 */
	void addNewPlot(int size, double electricityCount, double waterCount, String description) {
		throw new ExecutionControl.NotImplementedException("Not implemented jet!");
	}

	/**
	 * Updates the information about a {@link Plot} and saves them
	 * @param associatedPlot {@link Plot} that should to be changed
	 * @param description new description of {@link Plot} as String
	 * @param estimator new estimator of {@link Plot} of type {@link MonetaryAmount}
	 */
	void updatePlotInfo(Plot associatedPlot, String description, MonetaryAmount estimator) {
		throw new ExecutionControl.NotImplementedException("Not implemented jet!");
	}

	/**
	 *
	 * @return
	 */
	Streamable<Plot> getFreePlots() {
		throw new ExecutionControl.NotImplementedException("Not implemented jet!");
	}

	Streamable<Plot> getAssociatedPlots(Year year, Tenant tenant) {
		throw new ExecutionControl.NotImplementedException("Not implemented jet!");
	}

}
