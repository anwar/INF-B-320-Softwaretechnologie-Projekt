package kleingarten.plot;

import kleingarten.finance.Procedure;
import kleingarten.finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.salespointframework.useraccount.Role;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataService {
	private final TenantRepository tenantRepository;
	private final ProcedureManager procedureManager;
	private final PlotService plotService;

	DataService(TenantRepository tenantRepository, ProcedureManager procedureManager, PlotService plotService) {
		this.tenantRepository = tenantRepository;
		this.procedureManager = procedureManager;
		this.plotService = plotService;
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
	 * @return associated {@link Tenant} if it is found, else an exception of type {@link IllegalArgumentException} is thrown
	 */
	public Tenant findTenantById(long tenantId) {
		for (Tenant tenant:
			tenantRepository.findAll()) {
			if (tenant.getId() == tenantId) {
				return tenant;
			}
		}
		throw new IllegalArgumentException("Tenant must exist!");
	}

	public boolean tenantHasRole(Tenant tenant, Role role) {
		Set<Role> roles = findTenantById(tenant.getId()).getUserAccount().getRoles().toSet();
		for (Role tenantRole:
			 roles) {
			if (tenantRole.toString().equals(role.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the associated {@link Procedure} for a {@link Plot}
	 * @param year the year for which the {@link Procedure} should be found
	 * @param plot the {@link Plot} for which the {@link Procedure} should be found
	 * @return {@link Procedure} which is searched for
	 */
	public Procedure getProcedure(int year, Plot plot) {
		Procedure procedure = procedureManager.getProcedure(year, plot);
		if (procedure == null) {
			throw new IllegalArgumentException("Procedure must not be null!");
		}
		return procedure;
	}

	/**
	 * Check if there is a {@link Procedure} for the given {@link Plot} and year
	 * @param year year for which a {@link Procedure} is searched as int
	 * @param plot plot of type {@link Plot}for which a {@link Procedure} is searched
	 * @return true, if {@link Procedure} exists, else false
	 */
	public boolean procedureExists(int year, Plot plot) {
		Procedure procedure = procedureManager.getProcedure(year, plot);
		if (procedure == null) {
			return false;
		}
		return true;
	}

	/**
	 * Create a {@link Set} of {@link Plot}s which are rented by a specific {@link Tenant}
	 * @param year year for which the {@link Plot}s are searched as int
	 * @param tenant tenant of type {@link Tenant} who's rented {@link Plot}s are searched
	 * @return {@link Set} of {@link Plot}s which a associated to the given {@link Tenant}
	 */
	public Set<Plot> getRentedPlots(int year, Tenant tenant) {
		Set<Plot> rentedPlots = new HashSet<>();
		Set<Procedure> procedures = procedureManager.getProcedures(year, tenant.getId()).toSet();
		for (Procedure procedure : procedures) {
			rentedPlots.add(plotService.findById(procedure.getPlotId()));
		}
		return rentedPlots;
	}
}
