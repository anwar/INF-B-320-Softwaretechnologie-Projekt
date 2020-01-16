package kleingarten.finance;

import kleingarten.plot.Plot;
import kleingarten.plot.PlotService;
import kleingarten.plot.PlotStatus;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class ProcedureManager {

	private final ProcedureRepository procedures;
	//Add plotService to use it's methods (Ylvi)
	private final PlotService plotService;
	private final TenantManager tenantManager;


	@Autowired
	public ProcedureManager(ProcedureRepository procedures, PlotService plotService, TenantManager tenantManager) {
		Assert.notNull(procedures, "ProcedureRepository must not be null!");
		this.procedures = procedures;
		this.plotService = plotService;
		this.tenantManager = tenantManager;
	}

	public Streamable<Procedure> getAll() {
		return procedures.findAll();
	}

	public Procedure get(long id) {
		return procedures.findById(id);
	}

	//Changed the method so that the status of the associated plot is changed (Ylvi)
	public Procedure add(Procedure procedure) {
		plotService.findById(procedure.getPlotId()).setStatus(PlotStatus.TAKEN);
		return procedures.save(procedure);
	}

	/**
	 * Find a Procedure by a given year and Plot.
	 *
	 * @param year   for which to get the {@link Procedure}
	 * @param plotId is the Id for the {@link Plot}
	 * @return a {@link Procedure} or null if not found
	 */
	public Procedure getProcedure(int year, ProductIdentifier plotId) {
		for (Procedure procedure : procedures.findByPlotProductIdentifier(plotId)) {
			if (procedure.getYear() == year) return procedure;
		}

		return null;
	}

	public Procedure getProcedure(int year, Plot plot) {
		for (Procedure procedure : procedures.findByPlot(plot)) {
			if (procedure.getYear() == year) return procedure;
		}

		return null;
	}

	public Procedure getActualProcedure(Plot plot) {
		for (Procedure proc : procedures.findByPlot(plot)) {
			if (proc.isOpen()) return proc;
		}

		return null;
	}

	/**
	 * Get all Procedures for the Tenant, if he is main or sub Tenant in it.
	 *
	 * @param year     for which to get the {@link Procedure}s
	 * @param tenantId is the Id for the {@link Tenant}
	 * @return Empty {@link Streamable} or {@link Procedure}s
	 */
	public Streamable<Procedure> getProcedures(int year, long tenantId) {
		List<Procedure> procList = new LinkedList<Procedure>();

		for (Procedure procedure : procedures.findByYear(year)) {
			if (procedure.isTenant(tenantId)) procList.add(procedure);
		}
		return Streamable.of(procList);
	}

	/**
	 * Get all Procedures with the given TenantId inside.
	 * It doesnt matter if he is main or sub Tenant.
	 * Main Teanant Procedures will come first.
	 *
	 * @param tenantId is the Id for the {@link Tenant}
	 * @return (mainTenantProcedures):(subTenantProcedures)
	 */
	public Streamable<Procedure> getAll(long tenantId) {
		//I am preventing duplicates here. however, a tenant should only be main or sub tenant, not both.
		Set<Procedure> noDubes =
				procedures.findByMainTenant(tenantId).and(procedures.findBySubTenant(tenantId)).toSet();
		return Streamable.of(noDubes);
	}

	public Streamable<Procedure> getAllByYear(int year) {
		return procedures.findByYear(year);
	}

	public Streamable<Procedure> getAllByPlot(Plot plot) {

		return procedures.findByPlot(plot);

	}

	public Procedure save(Procedure procedure) {

		return procedures.save(procedure);
	}

	public Streamable<Procedure> findByPlotName(String plotName) {
		return procedures.findByPlotName(plotName);
	}

	public Streamable<Procedure> findByPlotId(String plotName) {
		return null;//procedures.findByPlotsName(plotName);
	}

	public Procedure get(Long id) {
		return procedures.findById(id).orElse(null);
	}

	public PlotService getPlotService() {
		return plotService;
	}

	public TenantManager getTenantManager() {
		return tenantManager;
	}

}
