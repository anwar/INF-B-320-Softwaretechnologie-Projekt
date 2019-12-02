package kleingarten.plot;

import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.salespointframework.useraccount.Role;
import org.springframework.stereotype.Component;

@Component
public class DataService {
	private final TenantRepository tenantRepository;

	DataService(TenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;
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
		for (Role tenantRole:
			 findTenantById(tenant.getId()).getUserAccount().getRoles()) {
			if (tenantRole == role) {
				return true;
			}
		}
		return false;
	}
}
