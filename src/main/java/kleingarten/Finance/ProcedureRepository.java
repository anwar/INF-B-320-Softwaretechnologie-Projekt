package kleingarten.Finance;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface ProcedureRepository extends CrudRepository<Procedure, Long> {

	Streamable<Procedure> findAll();

	Procedure findById(long id);

	Streamable<Procedure> findByPlotId(String plotId);

	Streamable<Procedure> findByYear(int year);

	//Procedure findByPlotId(String plotId);

	//find billid + year
	//find

}
