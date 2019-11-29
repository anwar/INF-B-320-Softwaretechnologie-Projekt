package kleingarten.Finance;

import java.util.LinkedList;
import java.util.List;

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

	public Streamable<Procedure> getAll() {
		return procedures.findAll();
	}

	public Procedure get(long id) {
		return procedures.findById(id);
	}

	public Procedure add(Procedure procedure) {
		return procedures.save(procedure);
	}
	
	/**
	 * Find a Procedure by a given year and plotId.
	 * 
	 * @param year
	 * @param plotId
	 * @return null if not found
	 */
	public Procedure getProcedure(int year, SalespointIdentifier plotId) {
		for(Procedure procedure:procedures.findByPlotId(plotId.getIdentifier())) {
			if(procedure.getYear() == year) return procedure;
		}
		
		return null;
	}
	
	
	public Streamable<Procedure> getProcedures(int year, long tenantId) {
		List<Procedure> procList = new LinkedList<Procedure>();
		
		for(Procedure procedure:procedures.findByYear(year)) {
			if(procedure.getMainTenant() == tenantId) procList.add(procedure);
		}
		return Streamable.of(procList);
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
