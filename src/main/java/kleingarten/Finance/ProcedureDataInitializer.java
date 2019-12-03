package kleingarten.Finance;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotCatalog;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcedureDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(ProcedureDataInitializer.class);
	private final ProcedureManager procedureManager;
	private final PlotCatalog plotCatalog;
	private final TenantRepository tenantRepository;

	ProcedureDataInitializer(ProcedureManager procedureManager, PlotCatalog plotCatalog, TenantRepository tenantRepository) {
		this.procedureManager = procedureManager;
		this.plotCatalog = plotCatalog;
		this.tenantRepository = tenantRepository;
	}

	@Override
	public void initialize() {
		if (!this.procedureManager.getAll().toList().isEmpty()) {
			return;
		}

		List<Plot> plots = this.plotCatalog.findAll().toList();
		List<Tenant> tenants = this.tenantRepository.findAll().toList();
		if (!plots.isEmpty() && !tenants.isEmpty()) {
			LOG.info("Creating default procedures");

			Tenant boss = null;
			Tenant obman = null;
			Tenant protocol = null;
			Tenant waterman = null;
			for (Tenant tenant:
				tenants) {
				if (tenant.getEmail().equals("peter.klaus@email.com")) {
					boss = tenant;
				} else if (tenant.getEmail().equals("hubert.grumpel2@cloud.com")) {
					obman = tenant;
				} else if (tenant.getEmail().equals("francys@email.com")) {
					protocol = tenant;
				} else if (tenant.getEmail().equals("neptuns.bart@fishmail.com")) {
					waterman = tenant;
				}
			}

			var procedure_1 = new Procedure(2019, plots.get(0), boss.getId());
			this.procedureManager.add(procedure_1);
			var procedure_2 = new Procedure(2019, plots.get(1), obman.getId());
			this.procedureManager.add(procedure_2);
			var procedure_3 = new Procedure(2019, plots.get(2), protocol.getId());
			this.procedureManager.add(procedure_3);
			var procedure_4 = new Procedure(2019, plots.get(3), waterman.getId());
			this.procedureManager.add(procedure_4);

			LOG.info("Finished creating default procedures");
		}
	}
}
