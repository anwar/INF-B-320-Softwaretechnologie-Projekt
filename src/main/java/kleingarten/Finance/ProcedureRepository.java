package kleingarten.Finance;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

public interface ProcedureRepository extends CrudRepository<Procedure, Long> {

	Streamable<Procedure> findAll();

	Procedure findById(long id);

	Streamable<Procedure> findByPlotId(String plotId);

	Streamable<Procedure> findByYear(int year);
	
	Streamable<Procedure> findByMainTenant( long tenantId);

	@Query("select p from Procedure p WHERE :tenant in elements(p.subTenants)")
	Streamable<Procedure> findBySubTenant(@Param("tenant") long tenantId);

}
