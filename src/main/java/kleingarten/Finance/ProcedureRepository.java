package kleingarten.Finance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface ProcedureRepository extends CrudRepository<Procedure, Long> {

	Streamable<Procedure> findAll();

	Procedure findById(long id);

	//find billid + year
	//find

}
