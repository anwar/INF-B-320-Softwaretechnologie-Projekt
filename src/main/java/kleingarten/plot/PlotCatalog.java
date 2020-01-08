package kleingarten.plot;

import kleingarten.tenant.Tenant;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

import java.util.Set;

/**
 * Extension of {@link Catalog} to add specific query methods
 */
public interface PlotCatalog extends Catalog<Plot> {
	Sort DEFAULT_SORT = Sort.by("name").ascending();

	/**
	 * Return all {@link Plot}s
	 *
	 * @return plots as {@link Streamable} of {@link Plot}, never {@literal null}
	 */
	@Override
	Streamable<Plot> findAll();

	/**
	 * Return all {@link kleingarten.plot.Plot}s sorted by given criteria
	 *
	 * @param sort {@link Sort} criteria which should be used to sort the {@link Plot}s
	 * @return plots as {@link Streamable} of {@link kleingarten.plot.Plot}, never {@literal null}
	 */
	Streamable<Plot> findAll(Sort sort);

	/**
	 * Return all {@link Plot}s sorted by their {@link org.salespointframework.catalog.ProductIdentifier}
	 *
	 * @return plots as {@link Streamable} of {@link Plot}, never {@literal null}
	 */
	default Streamable<Plot> getAll() {
		return findAll(DEFAULT_SORT);
	}

	/**
	 * Return all {@link Plot}s with the given status of type {@link PlotStatus}
	 *
	 * @param status status as {@link PlotStatus}, must not be {@literal null}
	 * @return plots as {@link Set} of {@link Plot}, never {@literal null}
	 */
	Set<Plot> findByStatus(PlotStatus status);

	/**
	 * Return all {@link Plot}s with the given chairman of type {@link Tenant}
	 *
	 * @param chairman chairman as {@link Tenant}, must not be {@literal null}
	 * @return plots as {@link Set} of {@link Plot}, never {@literal null}
	 */
	Set<Plot> findByChairman(Tenant chairman);

	/**
	 * Return the {@link Plot} with the given id as {@link org.salespointframework.catalog.ProductIdentifier}
	 *
	 * @param id id of the searched {@link Plot} as {@link org.salespointframework.catalog.ProductIdentifier}
	 * @return {@link Plot} with the given id
	 */
	Plot findByProductIdentifierId(String id);
}
