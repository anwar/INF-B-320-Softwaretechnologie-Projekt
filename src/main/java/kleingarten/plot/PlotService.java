package kleingarten.plot;

import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.Finance.Procedure;
import kleingarten.tenant.TenantRepository;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlotService {
	private final PlotCatalog plotCatalog;
	private final ProcedureManager procedureManager;
	private final TenantRepository tenantRepository;

	PlotService(PlotCatalog plotCatalog, ProcedureManager procedureManager, TenantRepository tenantRepository){
		this.plotCatalog = plotCatalog;
		this.procedureManager = procedureManager;
		this.tenantRepository = tenantRepository;
	}

	/**
	 * Create a new object of type {@link Plot} and add it to the {@link PlotCatalog}
	 * @param name name of the {@link Plot} as String
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
	 * Check if there is a {@link Plot} with the given name in the {@link PlotCatalog}
	 * @param name name of the {@link Plot} as String
	 * @return true, if {@link Plot} with the given name exists
	 */
	public boolean existsByName(String name) {
		return !this.plotCatalog.findByName(name).isEmpty();
	}

	/**
	 * Get the associated {@link Procedure} for a {@link Plot}
	 * @param year the year for which the {@link Procedure} should be found
	 * @param plotId the Id of the {@link Plot} for which the {@link Procedure} should be found
	 * @return {@link Procedure} which is searched for
	 */
	public Procedure getProcedure(int year, ProductIdentifier plotId) {
		Procedure procedure = procedureManager.getProcedure(year,plotId);
		if (procedure == null) {
			throw new IllegalArgumentException("Procedure must not be null!");
		}
		return procedure;
	}

	/**
	 * Compare if object of type {@link Tenant} is identical to an {@link Tenant} in the {@link TenantRepository}
	 * @param tenantId id as {@literal long} of the {@link Tenant}
	 * @return true, if there is a matching {@link Tenant}
	 */
	public boolean compareTenants(long tenantId) {
		for (Tenant user:
			 tenantRepository.findAll()) {
			if (user.getId() == tenantId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get object of type {@link Tenant} for a given tenantId
	 * @param tenantId id of the {@link Tenant} as {@literal long}
	 * @return associated {@link Tenant} if it is found, else {@literal null}
	 */
	public Tenant getTenant(long tenantId) {
		for (Tenant tenant:
			 tenantRepository.findAll()) {
			if (tenant.getId() == tenantId) {
				return tenant;
			}
		}
		throw new IllegalArgumentException("Tenant must exist!");
	}

}
