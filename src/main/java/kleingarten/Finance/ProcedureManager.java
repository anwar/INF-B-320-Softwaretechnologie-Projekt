package kleingarten.Finance;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ProcedureManager {

	private final ProcedureRepository procedures;

	/*
	@Autowired
	public ProcedureManager(ProcedureRepository procedures) {
		Assert.notNull(procedures, "ProcedureRepository must not be null!");
		this.procedures = procedures;
	}
	*/

	ProcedureManager(ProcedureRepository procedures){
		Assert.notNull(procedures, "TenantRepository must not be null!");
		this.procedures = procedures;
	}

	/*
		public Iterable<Procedure> findAll(){
		return procedures.findAll();
	}
	 */

	Streamable<Procedure> getAll() {
		return procedures.findAll();
	}

	Procedure get(long id) {
		return procedures.findById(id);
	}

	Procedure add(Procedure procedure) {
		return procedures.save(procedure);
	}


	// Currently there is an issue in plotId. It does not work.
	/*
	public Procedure save(Procedure procedure) {

		return procedures.save(procedure);
	}

	public Procedure findByPlotId(SalespointIdentifier plotId){
		return procedures.findByPlotId(plotId);
	}
	*/


}
