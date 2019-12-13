package kleingarten.plot;

import kleingarten.tenant.Tenant;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.util.Streamable;

import java.util.Set;

/**
 * Extension of {@link Catalog} to add specific query methods
 */
public interface PlotCatalog extends Catalog<Plot> {
	/**
	 * Return all {@link Plot}s
	 * @return plots as {@link Streamable} of {@link Plot}, never {@literal null}
	 */
	@Override
	Streamable<Plot> findAll();

	/**
	 * Return all {@link Plot}s with the given status of type {@link PlotStatus}
	 * @param status status as {@link PlotStatus}, must not be {@literal null}
	 * @return plots as {@link Set} of {@link Plot}, never {@literal null}
	 */
	Set<Plot> findByStatus(PlotStatus status);

	/**
	 * Return all {@link Plot}s with the given chairman of type {@link Tenant}
	 * @param chairman chairman as {@link Tenant}, must not be {@literal null}
	 * @return plots as {@link Set} of {@link Plot}, never {@literal null}
	 */
	Set<Plot> findByChairman(Tenant chairman);
}
