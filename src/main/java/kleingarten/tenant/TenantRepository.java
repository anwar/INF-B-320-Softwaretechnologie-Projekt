package kleingarten.tenant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;


public interface TenantRepository extends CrudRepository<Tenant, Long> {

	@Override
	Streamable<Tenant> findAll();
}
