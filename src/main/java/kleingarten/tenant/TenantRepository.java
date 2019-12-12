package kleingarten.tenant;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.Optional;


/**
 * Extension of {@link CrudRepository} to add specific query methods
 */
public interface TenantRepository extends CrudRepository<Tenant, Long> {

	/**
	 * Return all {@link Tenant}s
	 * @return {@link Streamable} of {@link Tenant}, never {@literal null}
	 */
	@Override
	Streamable<Tenant> findAll();

	/**
	 * Return all {@link Tenant}s with the given userAccount of type {@link UserAccount}
	 * @param userAccount userAccount as {@link UserAccount}, must not be {@literal null}
	 * @return tenants as {@link Streamable} of {@link Tenant}, never {@literal null}
	 */
	Optional<Tenant> findByUserAccount(UserAccount userAccount);

}
