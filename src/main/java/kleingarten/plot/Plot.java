package kleingarten.plot;

import org.salespointframework.catalog.Product;
import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Entity
public class Plot extends Product {

	private PlotStatus status;

	private int size;
	private String description;

	/**
	 * Private constructor of class {@link Plot}, which is used by the Spring Framework
	 */
	private Plot() {
		this.status = null;
		this.description = null;
	}

	/**
	 * Constructor of class {@link Plot}
	 * @param name number to identify the {@link Plot} as String to be easy readable by the user
	 * @param price estimator as {@link MonetaryAmount}, that represents the value of the {@link Plot}
	 * @param size size of the {@link Plot} as int
	 * @param description String that describes the look of a {@link Plot}
	 */
	public Plot(String name, MonetaryAmount price, int size, String description) {
		super(name, price);
		this.status = PlotStatus.FREE;
		this.size = size;
		this.description = description;
	}

	/**
	 * Getter for the status of a {@link Plot}
	 * @return status as Enum of type {@link PlotStatus}
	 */
	public PlotStatus getStatus() {
		return status;
	}

	/**
	 * Setter for the status of a {@link Plot}
	 * @param status status as {@link PlotStatus}
	 */
	public void setStatus(PlotStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("Status must not be null!");
		}
		this.status = status;
	}

	/**
	 * Getter for the size of a {@link Plot}
	 * @return size as int
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Setter for the size of a {@link Plot}
	 * @param size size as int
	 */
	public void setSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be positive!");
		}
		this.size = size;
	}

	/**
	 * Getter for the description of a {@link Plot}
	 * @return description as String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for the description of a {@link Plot}
	 * @param description description as String
	 */
	public void setDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Description must not be null!");
		}
		this.description = description;
	}

	/**
	 * Getter for the estimator (or price) of a {@link Plot}
	 * @return estimator as {@link MonetaryAmount}
	 */
	public MonetaryAmount getEstimator() {
		return this.getPrice();
	}

	/**
	 * Setter for the estimator (or price) of a {@link Plot}
	 * @param estimator estimator as {@link MonetaryAmount}
	 */
	public void setEstimator(MonetaryAmount estimator) {
		if (estimator == null) {
			throw new IllegalArgumentException("Estimator must not be null!");
		}
		if (estimator.isNegative()) {
			throw new IllegalArgumentException("Estimator must be positive!");
		}
		this.setPrice(estimator);
	}
}
