// Copyright 2019-2020 the original author or authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package kleingarten.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationManager {

	private final ApplicationRepository repository;

	@Autowired
	public ApplicationManager(ApplicationRepository repository) {
		this.repository = repository;
	}

	public List<Application> getAll() {
		return repository.findAll().toList();
	}

	public Application getById(Long id) {
		Application app = repository.findById(id).get();
		return app;
	}

	public List<Application> getByPlotId(String plotId) {
		return repository.findByPlotId(plotId).toList();
	}

	public void printAllToConsole() {
		for (Application application : getAll()) {
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
		for (Application app : allApplications) {
			if (app.getState() == ApplicationState.ACCEPTED)
				return;
		}

		application.accept(); //accept selected one
		repository.save(application);

		for (Application app : allApplications) {
			if (app.getId() == application.getId()) continue;

			app.deny(); //deny all others, selected isnt new anymore
			repository.save(app);
		}
	}
}
