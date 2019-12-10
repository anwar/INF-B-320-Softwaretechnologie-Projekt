package kleingarten.finance;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.lang.NonNull;

import kleingarten.plot.Plot;

@Entity
public class Procedure {


	private @Id @GeneratedValue long id;

	/**
	 * Year is needed to get the prices for some items
	 */
	private int year;

	/**
	 * if the procedure is open, it can be edited.
	 * When the bill gets calculated, the procedure closes and should not be opened or edited anymore.
	 */
	private boolean isOpen;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Plot plot;

	/**
	 * The values shown by the clock.
	 * The gap between this and last year is the fee.
	 */
	private double
			watercount,
			powercount;
	/**
	 * Size of the plot in qm.
	 * May also change from year to year.
	 */
	private double size;

	/**
	 * work hours are count in minutes to get better overview.
	 * We need to round it down to half hours in the fee calculation.
	 */
	private int workMinutes;

	/**
	 * The main tenant id. Can not be null.
	 */
	private @NonNull long mainTenant;

	/**
	 * Set of sub Tenant IDs.
	 * Empty Set if only main tenant.
	 */
	@ElementCollection //Siehe Dokumentation ElementCollection, wurde auch analog im Salespoint-Produkt verwendet.
	private Set<Long> subTenants = new HashSet<Long>();

	private Procedure() {
		//Default Constructor for spring
		super();
	}

	/**
	 * Constructor with needed types. Good for testing.
	 *
	 * @param year
	 * @param plotId
	 * @param size
	 * @param mainTenant
	 */
	public Procedure(int year, String plotId, double size, long mainTenant) {
		super();
		this.year = year;
		this.size = size;
		this.mainTenant = mainTenant;
		isOpen = true;
	}

	/**
	 * Constructor with some parsing, best to use this one.
	 *
	 * @param year
	 * @param plot
	 * @param mainTenant
	 */
	public Procedure(int year, Plot plot, long mainTenant) {
		this(year, plot.getId().getIdentifier(), (double)plot.getSize(), mainTenant);
		this.plot = plot;
	}


	public boolean isTenant(long tenantId) {
		if(mainTenant==tenantId) return true;
		if(subTenants.contains(tenantId)) return true;
		return false;
	}


	public double getWatercount() {
		return watercount;
	}

	public void setWatercount(double watercount) {
		if(!editable()) return;
		this.watercount = watercount;
	}

	public double getPowercount() {
		return powercount;
	}

	public void setPowercount(double powercount) {
		if(!editable()) return;
		this.powercount = powercount;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		if(!editable()) return;
		this.size = size;
	}

	public int getWorkMinutes() {
		return workMinutes;
	}

	public void setWorkMinutes(int workMinutes) {
		if(!editable()) return;
		this.workMinutes = workMinutes;
	}

	public long getId() {
		return id;
	}

	public int getYear() {
		return year;
	}

	public long getMainTenant() {
		return mainTenant;
	}

	public Set<Long> getSubTenants() {
		return subTenants;
	}

	public ProductIdentifier getPlotId() {
		//Return right data (Ylvi)
		return plot.getId();
	}

	/**
	 * Add a new Sub Tenant to the Procedure.
	 *
	 * @param tenantID
	 * @return true when added, false if not
	 */
	public boolean addSubTenant(long tenantID) {
		if(!editable()) return false;

		//should not set main Tenant as sub Tenant
		if (tenantID==mainTenant) return false;

		//Set restricts duplicates
		return subTenants.add(tenantID);

	}

	/**
	 * Remove a sub Tenant out of Tenant List, will not affect main Tenant.
	 *
	 * @param tenantID of Tenant to be removed
	 * @return true if removed
	 */
	public boolean removeSubTenant(long tenantID) {
		if(!editable()) return false;

		return subTenants.remove(tenantID);
	}

	/**
	 * Set a main Tenant for the Process, the old Tenant will be overwritten.
	 * Also keep the subTenants as they are.
	 *
	 * @param tenantID
	 * @return false if tenant is already main tenant
	 */
	public boolean setMainTenant(long tenantID) {
		if(!editable()) return false;
		if(tenantID==mainTenant) return false;

		mainTenant = tenantID;
		return true;
	}

	/**
	 * Set the new main Tenant for the Process.
	 * Will also remove all sub Tenants.
	 *
	 * @param tenantID
	 * @return false if its the same main Tenant as before
	 */
	public boolean setNewMainTenant(long tenantID) {
		if(!editable()) return false;

		if(tenantID==mainTenant) {
			return false;
		}

		mainTenant=tenantID;
		subTenants.clear();
		return true;

	}

	private boolean editable() {
		return isOpen;
	} //this seems to be redundant, but it has internal use and may differ from isOpen later..

	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Close the Procedure, it cant be edited anymore.
	 */
	public void close() {
		isOpen=false;
	}



	public String toString() {
		return "Procedure Plot: " + plot.getName() + " Tenant: " + mainTenant + " isOpen? " + isOpen();
	}

}
