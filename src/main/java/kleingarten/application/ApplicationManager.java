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
	
	public Application getById(Long id) {
		Application app = repository.findById(id).get();
		return app;
	}
	
	public List<Application> getByPlotId(String plotId){
		return repository.findByPlotId(plotId).toList();
	}
	
	public void printAllToConsole() {
		for(Application application:getAll()) {
			System.out.println(application.toString());
		}
	}
	
	public void add(Application application) {
		repository.save(application);
	}
	
	public void save(Application application) {
		repository.save(application);
	}
	
	public void accept(Application application) {
		
		List<Application> allApplications = getByPlotId(application.getPlotId());
		
		//only one accepted is allowed
		for(Application app:allApplications) {
			if(app.getState() == ApplicationState.ACCEPTED)
				return;
		}
		
		application.accept(); //accept selected one
		repository.save(application);
		
		for(Application app:allApplications) {
			if(app.getId()==application.getId()) continue;
			
			app.deny(); //deny all others, selected isnt new anymore
			repository.save(app);
		}
	}
}
