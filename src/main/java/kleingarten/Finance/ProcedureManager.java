package kleingarten.Finance;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ProcedureManager {

	private final ProcedureRepository procedures;

	ProcedureManager(ProcedureRepository procedures){

		Assert.notNull(procedures, "TenantRepository must not be null!");
		
		this.procedures = procedures;
		
	}
	
	Streamable<Procedure> getAll() {
		return procedures.findAll();
	}
	
	Procedure get(long id) {
		return procedures.findById(id);
	}
	
	Procedure add(Procedure procedure) {
		return procedures.save(procedure);
	}
	
	
}
