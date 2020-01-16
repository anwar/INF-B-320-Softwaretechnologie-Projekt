package kleingarten.finance;

import kleingarten.plot.Plot;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Procedure {

	@Id
	@GeneratedValue
	private long id;

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
	private @NonNull
	long mainTenant;

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
	 * Constructor with some parsing, best to use this one.
	 *
	 * @param year       as {@link Integer}
	 * @param plot       as {@link Plot}
	 * @param mainTenant as {@link Long}
	 */
	public Procedure(int year, Plot plot, long mainTenant) {
		this.year = year;
		this.size = plot.getSize();
		this.mainTenant = mainTenant;
		isOpen = true;
		this.plot = plot;
	}

	/**
	 * Method to check whether a user is a tenant
	 *
	 * @param tenantId Id of the {@link kleingarten.tenant.Tenant}
	 * @return {@link Boolean}
	 */
	public boolean isTenant(long tenantId) {
		if (mainTenant == tenantId) return true;
		return subTenants.contains(tenantId);
	}

	/**
	 * Getter for the watercount as {@link Double}
	 *
	 * @return watercount as {@link Double}
	 */
	public double getWatercount() {
		return watercount;
	}

	/**
	 * Setter for the watercount as {@link Double}
	 *
	 * @param watercount as {@link Double}
	 */
	public void setWatercount(double watercount) {
		if (!editable()) return;
		this.watercount = watercount;
	}

	/**
	 * Getter for the powercount as {@link Double}
	 *
	 * @return powercount as {@link Double}
	 */
	public double getPowercount() {
		return powercount;
	}

	/**
	 * Setter for the powercount as {@link Double}
	 *
	 * @param powercount as {@link Double}
	 */
	public void setPowercount(double powercount) {
		if (!editable()) return;
		this.powercount = powercount;
	}

	/**
	 * Getter for the size as {@link Double}
	 *
	 * @return size as {@link Double}
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Setter for the size as {@link Double}
	 *
	 * @param size as {@link Double}
	 */
	public void setSize(double size) {
		if (!editable()) return;
		this.size = size;
	}

	/**
	 * Getter for the workMinutes as {@link Integer}
	 *
	 * @return workMinutes as {@link Integer}
	 */
	public int getWorkMinutes() {
		return workMinutes;
	}

	/**
	 * Setter for the workMinutes as {@link Integer}
	 *
	 * @param workMinutes as {@link Integer}
	 */
	public void setWorkMinutes(int workMinutes) {
		if (!editable()) return;
		this.workMinutes = workMinutes;
	}

	/**
	 * Getter for the id as {@link Long}
	 *
	 * @return id as {@link Long}
	 */
	public long getId() {
		return id;
	}

	/**
	 * Getter for the year as {@link Integer}
	 *
	 * @return year as {@link Integer}
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Getter for the mainTenant as {@link kleingarten.tenant.Tenant}
	 *
	 * @return mainTenant as {@link Long}
	 */
	public long getMainTenant() {
		return mainTenant;
	}

	/**
	 * Getter for the subTenant as {@link kleingarten.tenant.Tenant}
	 *
	 * @return subTenant as {@link Long}
	 */
	public Set<Long> getSubTenants() {
		return subTenants;
	}

	public ProductIdentifier getPlotId() {
		//Return right data (Ylvi)
		return plot.getId();
	}

	public Plot getPlot() {
		return plot;
	}

	/**
	 * Add a new Sub Tenant to the Procedure.
	 *
	 * @param tenantID as {@link Long}
	 * @return true when added, false if not as {@link Long}
	 */
	public boolean addSubTenant(long tenantID) {
		if (!editable()) return false;

		//should not set main Tenant as sub Tenant
		if (tenantID == mainTenant) return false;

		//Set restricts duplicates
		return subTenants.add(tenantID);

	}

	/**
	 * Remove a sub Tenant out of Tenant List, will not affect main Tenant.
	 *
	 * @param tenantID of Tenant to be removed as {@link Long}
	 * @return true if removed as {@link Long}
	 */
	public boolean removeSubTenant(long tenantID) {
		if (!editable()) return false;

		return subTenants.remove(tenantID);
	}

	/**
	 * Set a main Tenant for the Process, the old Tenant will be overwritten.
	 * Also keep the subTenants as they are.
	 *
	 * @param tenantID as {@link Long}
	 * @return false if tenant is already main tenant as {@link Boolean}
	 */
	public boolean setMainTenant(long tenantID) {
		if (!editable()) return false;
		if (tenantID == mainTenant) return false;

		mainTenant = tenantID;
		return true;
	}

	/**
	 * Set the new main Tenant for the Process.
	 * Will also remove all sub Tenants.
	 *
	 * @param tenantID as {@link Long}
	 * @return as {@link Boolean}
	 */
	public boolean setNewMainTenant(long tenantID) {
		if (!editable()) return false;

		if (tenantID == mainTenant) {
			return false;
		}

		mainTenant = tenantID;
		subTenants.clear();
		return true;

	}

	/**
	 *  Method to check whether a procedure is opened
	 *  Open means that the opened procedure can be edited
	 *
	 * @return {@link Boolean}
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Close the Procedure, it can't be edited anymore
	 */
	public void close() {
		isOpen = false;
	}

	/**
	 *  Method to check whether a procedure is opened or closed
	 *  if editable() is true, then the procedure is opened and can be edited
	 *  if editable() is false, then the procedure is closed and can't be edited
	 *
	 * @return {@link Boolean}
	 */
	private boolean editable() {
		return isOpen;
	}

	public String toString() {
		return "Procedure Plot: " + plot.getName() + " Tenant: " + mainTenant + " isOpen? " + isOpen();
	}

}
