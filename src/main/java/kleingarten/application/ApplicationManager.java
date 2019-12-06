package kleingarten.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationManager {
	
	private final ApplicationRepository repository;
	
	@Autowired
	public ApplicationManager(ApplicationRepository repository) {
		this.repository = repository;
	}
	
	public List<Application> getAll(){
		return repository.findAll().toList();
	}
	
	public List<Application> getByPlotId(String plotId){
		return repository.findByPlotId(plotId).toList();
	}
}
