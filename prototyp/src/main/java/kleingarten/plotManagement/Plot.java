package kleingarten.plotManagement;


import kleingarten.tenant.Tenant;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import java.util.ArrayList;

import static org.salespointframework.core.Currencies.EURO;

@Entity
public class Plot extends Product {

	private int size;
	private String description;
	private MonetaryAmount estimator;
	private PlotStatus status;
	@OneToOne
	private Tenant tenant;

	private Plot() {
	}

	/**
	 * Creates new plot
	 *
	 * @param size        represents the size of the plot
	 * @param indicator   name of the plot
	 * @param description short text to describe the look of the plot
	 */
	public Plot(int size, String indicator, String description) {
		super(indicator, Money.of(0, EURO));
		this.size = size;
		this.setDescription(description);
		this.setStatus(PlotStatus.FREE);
	}

	/**
	 * Get size of plot
	 *
	 * @return size as int
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Get value of plot
	 *
	 * @return value as MonetaryAmount
	 */
	public MonetaryAmount getEstimator() {
		return estimator;
	}

	/**
	 * Set value of plot
	 *
	 * @param estimator estimator to describe the value of the plot
	 */
	public void setEstimator(MonetaryAmount estimator) {
		this.estimator = estimator;
	}


	/**
	 * Get description of plot
	 *
	 * @return description as String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set description of plot
	 *
	 * @param description short text, which describes the look of the plot
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get status of plot
	 *
	 * @return status as enum
	 */
	public PlotStatus getStatus() {
		return status;
	}

	/**
	 * Set status of plot
	 *
	 * @param status enum, which describes the status of the plot
	 */
	public void setStatus(PlotStatus status) {
		this.status = status;
	}

	/**
	 * Get main tenant of plot
	 * @return tenant as Tenant object
	 */
	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * Set main tenant of plot
	 * @param tenant tenant, which should rent plot
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
		this.setStatus(PlotStatus.TAKEN);
	}

	/**
	 * Check if given tenant is equal to main tenant of plot
	 * @param tenant tenant that should be equal to main tenant
	 * @return boolean that shows if check was successful
	 */
	public boolean matchesTenant(Tenant tenant) {
		if (this.tenant == null) {
			return false;
		}
		return this.tenant.equals(tenant);
	}
}
