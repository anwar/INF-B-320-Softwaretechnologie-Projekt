package kleingarten.plotManagement;

import kleingarten.tenant.Tenant;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class PlotService {

	private final PlotCatalog plots;

	PlotService(PlotCatalog plots){

		Assert.notNull(plots, "CustomerRepository must not be null!");

		this.plots = plots;
	}

	public Plot createPlot(PlotRegistrationForm form){

		Assert.notNull(form, "Registration form must not be null!");

		return plots.save(new Plot(Integer.parseInt(form.getSize()), form.getIndicator(), form.getDescription()));
	}

	public Streamable<Plot> getAll() {
		return plots.findAll();
	}





	public Plot get(String id) {
		return plots.findByName(id).get().findFirst().orElse(null);
	}

	public void updateTenant(Plot plot, Tenant tenant) {
		plot.setTenant(tenant);
		this.plots.save(plot);
	}

}
