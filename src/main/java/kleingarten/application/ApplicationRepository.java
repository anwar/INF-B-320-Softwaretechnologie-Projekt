package kleingarten.application;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface ApplicationRepository extends CrudRepository<Application, Long> {
	
	public Streamable<Application> findAll();
	
	public Streamable<Application> findByPlotId(String plotId);
	
	

}
