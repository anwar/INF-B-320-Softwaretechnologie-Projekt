package kleingarten.plot;

import kleingarten.Finance.Procedure;
import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.salespointframework.useraccount.Role;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataService {
	private final TenantRepository tenantRepository;
	private final ProcedureManager procedureManager;

	DataService(TenantRepository tenantRepository, ProcedureManager procedureManager) {
		this.tenantRepository = tenantRepository;
		this.procedureManager = procedureManager;
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
}
