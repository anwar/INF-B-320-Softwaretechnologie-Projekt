package kleingarten.Finance;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.salespointframework.core.SalespointIdentifier;
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
	
	public Procedure getProcedure(int year, String plotId) {
		for(Procedure procedure:procedures.findByPlotId(plotId)) {
			if(procedure.getYear() == year) return procedure;
		}

		return null;
	}

	/**
	 * Get all Procedures for the Tenant, if he is main or sub Tenant in it.
	 *
	 * @param year
	 * @param tenantId
	 * @return Empty Steamable or Procedures
	 */
	public Streamable<Procedure> getProcedures(int year, long tenantId) {
		List<Procedure> procList = new LinkedList<Procedure>();

		for(Procedure procedure:procedures.findByYear(year)) {
			if(procedure.isTenant(tenantId)) procList.add(procedure);
		}
		return Streamable.of(procList);
	}
	
	/**
	 * Get all Procedures with the given TenantId inside.
	 * It doesnt matter if he is main or sub Tenant.
	 * Main Teanant Procedures will come first.
	 * 
	 * @param tenantId
	 * @return (mainTenantProcedures):(subTenantProcedures)
	 */
	public Streamable<Procedure> getAll(long tenantId) {
		//I am preventing duplicates here. however, a tenant should only be main or sub tenant, not both.
		Set<Procedure> noDubes = procedures.findByMainTenant(tenantId).and(procedures.findBySubTenant(tenantId)).toSet();
		return Streamable.of(noDubes);
	}


	// Currently there is an issue in plotId. It does not work.

	public Procedure save(Procedure procedure) { //?? why do we need this?[Sascha]

		return procedures.save(procedure);
	}

	public Streamable<Procedure> findByPlotId(String plotId){
		return procedures.findByPlotId(plotId);
	}




}
