package kleingarten.Finance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends CrudRepository<Fee, Long> {
	//Streamable<Fee> findByTitle(String title);

}
