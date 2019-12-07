/*package kleingarten.complains;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// WIP we still have to do this, but if someone got time, they can start working on this
@Service
public class ComplainsManager {

	private final ComplainsRepository complainsRepository;

	@Autowired
	public ComplainsManager(ComplainsRepository complainsRepository){
		this.complainsRepository = complainsRepository;
	}

	public List<Complains> getAll(){
		 return complainsRepository.findAll();
	}
}*/
