package kleingarten.Finance;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.util.Streamable;

import kleingarten.tenant.Tenant;

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
	
	private Tenant mainTenant;
	private Streamable<Tenant> subTenants;
	
	

}
