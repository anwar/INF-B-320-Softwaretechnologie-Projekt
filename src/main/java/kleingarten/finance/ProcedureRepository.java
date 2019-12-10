package kleingarten.finance;

import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

import kleingarten.plot.Plot;

public interface ProcedureRepository extends CrudRepository<Procedure, Long> {

	Streamable<Procedure> findAll();

	Procedure findById(long id);

	Streamable<Procedure> findByPlot(Plot plot);

	Streamable<Procedure> findByPlotName(String plotName);
	

	Streamable<Procedure> findByPlotProductIdentifier(ProductIdentifier plotId);
	
	

	Streamable<Procedure> findByYear(int year);
	
	Streamable<Procedure> findByMainTenant( long tenantId);

	@Query("select p from Procedure p WHERE :tenant in elements(p.subTenants)")
	Streamable<Procedure> findBySubTenant(@Param("tenant") long tenantId);

}
