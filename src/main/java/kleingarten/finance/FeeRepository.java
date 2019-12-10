package kleingarten.finance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends CrudRepository<Fee, Long> {
	//Streamable<Fee> findByTitle(String title);
	List<Fee> findAll();

}
