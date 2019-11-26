package kleingarten.plot;

import org.salespointframework.catalog.Product;
import javax.money.MonetaryAmount;

public class Plot extends Product {

	private PlotStatus status;

	private int size;
	private String description;

	//ElectricityCount and WaterCount have duplicates to save the value of the counter and the difference since the year before
	private double electricityCount = -1;
	private double electricityCountDifference;
	private double waterCount = -1;
	private double waterCountDifference;

	/**
	 * Private constructor of class Plot, which is used by the Spring Framework
	 */
	private Plot() {
		this.status = null;
		this.description = null;
	}

	/**
	 * Constructor of class Plot
	 * @param name number to identify the plot as String to be easy readable by the user
	 * @param price estimator as MonetaryAmount, that represents the value of the plot
	 * @param size size of the plot as int
	 * @param description String that describes the look of a plot
	 * @param electricityCount value of the electric meter of a plot as double
	 * @param waterCount value of the water meter of a plot as double
	 */
	public Plot(String name, MonetaryAmount price, int size, String description, double electricityCount, double waterCount) {
		super(name, price);
		this.status = PlotStatus.FREE;
		this.size = size;
		this.description = description;
		this.electricityCount = electricityCount;
		this.waterCount = waterCount;
	}

	/**
	 * Getter for the status of a plot
	 * @return status as Enum of type {@link PlotStatus}
	 */
	public PlotStatus getStatus() {
		return status;
	}

	/**
	 * Setter for the status of a plot
	 * @param status status as {@link PlotStatus}
	 */
	public void setStatus(PlotStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("Status must not be null!");
		}
		this.status = status;
	}

	/**
	 * Getter for the size of a plot
	 * @return size as int
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Setter for the size of a plot
	 * @param size size as int
	 */
	public void setSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be positive!");
		}
		this.size = size;
	}

	/**
	 * Getter for the description of a plot
	 * @return description as String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for the description of a plot
	 * @param description description as String
	 */
	public void setDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Description must not be null!");
		}
		this.description = description;
	}

	/**
	 * Getter for the estimator (or price) of a plot
	 * @return estimator as {@link MonetaryAmount}
	 */
	public MonetaryAmount getEstimator() {
		return this.getPrice();
	}

	/**
	 * Setter for the estimator (or price) of a plot
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

	/**
	 * Getter for the electricityCount (value of the electric meter) of a plot
	 * @return electricityCount as double
	 */
	public double getElectricityCount() {
		return electricityCount;
	}

	/**
	 * Setter for the electricityCount (value of the electric meter) of a plot
	 * @param electricityCount electricityCount as double
	 */
	public void setElectricityCount(double electricityCount) {
		if (electricityCount < 0) {
			throw new IllegalArgumentException("ElectricityCount must not be negative!");
		}
		this.electricityCount = electricityCount;
	}

	/**
	 * Getter for the electricityCountDifference (difference of the electric meter value since the last year) of a plot
	 * @return electricityCountDifference as double
	 */
	public double getElectricityCountDifference() {
		return electricityCountDifference;
	}

	/**
	 * Setter for the electricityCountDifference (difference of the electric meter value since the last year) of a plot
	 * @param electricityCount value of the electric meter of this year as double
	 */
	public void setElectricityCountDifference(double electricityCount) {
		if (electricityCount < this.electricityCount) {
			throw new IllegalArgumentException("ElectricityCount must not be negative!");
		}
		this.electricityCountDifference = electricityCount - this.electricityCount;
		this.setElectricityCount(electricityCount);
	}

	/**
	 * Getter for the waterCount (value of the water meter) of a plot
	 * @return waterCount as double
	 */
	public double getWaterCount() {
		return waterCount;
	}

	/**
	 * Setter for the waterCount (value of the water meter) of a plot
	 * @param waterCount waterCount as double
	 */
	public void setWaterCount(double waterCount) {
		if (waterCount < 0) {
			throw new IllegalArgumentException("WaterCount must not be negative!");
		}
		this.waterCount = waterCount;
	}

	/**
	 * Getter for the waterCountDifference (difference of the water meter value since the last year) of a plot
	 * @return waterCountDifference as double
	 */
	public double getWaterCountDifference() {
		return waterCountDifference;
	}

	/**
	 * Setter for the waterCountDifference (difference of the water meter value since the last year) of a plot
	 * @param waterCount value of the water meter of this year as double
	 */
	public void setWaterCountDifference(double waterCount) {
		if (waterCount < this.waterCount) {
			throw new IllegalArgumentException("WaterCount must not be negative!");
		}
		this.waterCountDifference = waterCount - this.waterCount;
		this.setWaterCount(waterCount);
	}
}
