package kleingarten.Finance;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.useraccount.Role;
import org.springframework.data.util.Streamable;
import org.springframework.lang.NonNull;

import kleingarten.tenant.Tenant;

@Entity
public class Procedure {
	

	private @Id @GeneratedValue long id;
	
	/**
	 * Year is needed to get the prices for some items
	 */
	private int year;
	
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
	protected Set<Long> subTenants = new HashSet();

	public Procedure(int year, double size, long mainTenant) {
		super();
		this.year = year;
		this.size = size;
		this.mainTenant = mainTenant;
	}

	public double getWatercount() {
		return watercount;
	}

	public void setWatercount(double watercount) {
		this.watercount = watercount;
	}

	public double getPowercount() {
		return powercount;
	}

	public void setPowercount(double powercount) {
		this.powercount = powercount;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public int getWorkMinutes() {
		return workMinutes;
	}

	public void setWorkMinutes(int workMinutes) {
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
	

}
