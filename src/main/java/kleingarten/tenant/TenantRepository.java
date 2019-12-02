package kleingarten.tenant;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;


public interface TenantRepository extends CrudRepository<Tenant, Long> {

	@Override
	Streamable<Tenant> findAll();

	Optional<Tenant> findByUserAccount(UserAccount userAccount);
}
