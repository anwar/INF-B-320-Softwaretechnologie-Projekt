package kleingarten.plotManagement;


import org.salespointframework.catalog.Catalog;
import org.springframework.data.util.Streamable;
import kleingarten.tenant.Tenant;

public interface PlotCatalog extends Catalog<Plot> {

	@Override
	Streamable<Plot> findAll();

	Streamable<Plot> findByTenant(Tenant tenant);

}
