package kleingarten.plot;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.util.Streamable;

/**
 * Extension of {@link Catalog} to add specific query methods
 */
public interface PlotCatalog extends Catalog<Plot> {
	/**
	 * Returns all plots
	 * @return plots as {@link Streamable} of {@link Plot}, never {@literal null}
	 */
	@Override
	Streamable<Plot> findAll();

	/**
	 * Returns all plots with the given name. There should only be one plot with this name.
	 * @param name name as String, must not be {@literal null}
	 * @return plot as {@link Streamable} of {@link Plot}, never {@literal null}
	 */
	@Override
	Streamable<Plot> findByName(String name);

	/**
	 * Returns all plot with the given status of type {@link PlotStatus}
	 * @param status status as {@link PlotStatus}, must not be {@literal null}
	 * @return plots as {@link Streamable} of {@link Plot}, never {@literal null}
	 */
	Streamable<Plot> findByStatus(PlotStatus status);
}
