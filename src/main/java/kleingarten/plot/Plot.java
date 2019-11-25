package kleingarten.plot;

import org.salespointframework.catalog.Product;
import javax.money.MonetaryAmount;

public class Plot extends Product {

	private PlotStatus status;

	private int size;
	private String description;

	private double electricityCount = -1;
	private double electricityCountDifference;
	private double waterCount = -1;
	private double waterCountDifference;

	private Plot() {
		this.status = null;
		this.description = null;
	}

	public Plot(String name, MonetaryAmount price, int size, String description, double electricityCount, double waterCount) {
		super(name, price);
		this.status = PlotStatus.FREE;
		this.size = size;
		this.description = description;
		this.electricityCount = electricityCount;
		this.waterCount = waterCount;
	}

	public PlotStatus getStatus() {
		return status;
	}

	public void setStatus(PlotStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("Status must not be null!");
		}
		this.status = status;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be positive!");
		}
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Description must not be null!");
		}
		this.description = description;
	}

	public MonetaryAmount getEstimator() {
		return this.getPrice();
	}

	public void setEstimator(MonetaryAmount estimator) {
		if (estimator == null) {
			throw new IllegalArgumentException("Estimator must not be null!");
		}
		if (estimator.isNegative()) {
			throw new IllegalArgumentException("Estimator must be positive!");
		}
		this.setPrice(estimator);
	}

	public double getElectricityCount() {
		return electricityCount;
	}

	public void setElectricityCount(double electricityCount) {
		if (electricityCount < 0) {
			throw new IllegalArgumentException("ElectricityCount must not be negative!");
		}
		this.electricityCount = electricityCount;
	}

	public double getElectricityCountDifference() {
		return electricityCountDifference;
	}

	public void setElectricityCountDifference(double electricityCount) {
		if (electricityCount < this.electricityCount) {
			throw new IllegalArgumentException("ElectricityCount must not be negative!");
		}
		this.electricityCountDifference = electricityCount - this.electricityCount;
		this.setElectricityCount(electricityCount);
	}

	public double getWaterCount() {
		return waterCount;
	}

	public void setWaterCount(double waterCount) {
		if (waterCount < 0) {
			throw new IllegalArgumentException("WaterCount must not be negative!");
		}
		this.waterCount = waterCount;
	}

	public double getWaterCountDifference() {
		return waterCountDifference;
	}

	public void setWaterCountDifference(double waterCount) {
		if (waterCount < this.waterCount) {
			throw new IllegalArgumentException("WaterCount must not be negative!");
		}
		this.waterCountDifference = waterCount - this.waterCount;
		this.setWaterCount(waterCount);
	}
}
