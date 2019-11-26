package kleingarten.plot;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.MonetaryAmount;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.salespointframework.core.Currencies.EURO;

public class PlotTest {
	private Plot plot;

	/**
	 * Setup of a mock of type plot to run the tests with
	 */
	@BeforeEach
	public void setup() {
		plot = new Plot("123", Money.of(300, EURO), 500, "test", 5, 6);
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void testInitial() {
		assertThat(plot.getStatus()).isEqualTo(PlotStatus.FREE);
		assertThat(plot.getSize()).isEqualTo(500);
		assertThat(plot.getDescription()).isEqualTo("test");
		assertThat(plot.getEstimator()).isEqualTo(Money.of(300, EURO));
		assertThat(plot.getElectricityCount()).isEqualTo(5);
		assertThat(plot.getElectricityCountDifference()).isEqualTo(0);
		assertThat(plot.getWaterCount()).isEqualTo(6);
		assertThat(plot.getWaterCountDifference()).isEqualTo(0);
	}

	/**
	 * Test if changes of the plot are made correctly
	 */
	@Test
	public void testChanges() {
		plot.setStatus(PlotStatus.TAKEN);
		plot.setSize(300);
		plot.setDescription("This plot is rented");
		plot.setEstimator(Money.of(400, EURO));
		plot.setElectricityCountDifference(120.5);
		plot.setWaterCountDifference(200.5);

		assertThat(plot.getStatus()).isEqualTo(PlotStatus.TAKEN);
		assertThat(plot.getSize()).isEqualTo(300);
		assertThat(plot.getDescription()).isEqualTo("This plot is rented");
		assertThat(plot.getEstimator()).isEqualTo(Money.of(400, EURO));
		assertThat(plot.getElectricityCount()).isEqualTo(120.5);
		assertThat(plot.getElectricityCountDifference()).isEqualTo(115.5);
		assertThat(plot.getWaterCount()).isEqualTo(200.5);
		assertThat(plot.getWaterCountDifference()).isEqualTo(194.5);
	}

	/**
	 * Test if illegal values for the plots attributes lead to exceptions of type {@link IllegalArgumentException}
	 */
	@Test
	public void testIllegalArguments() {
		PlotStatus status = null;
		int size_negative = -5;
		int size = 0;
		String description = null;
		MonetaryAmount estimator_negative = Money.of(-5, EURO);
		MonetaryAmount estimator = null;
		double electricityCount_negative = -2.5;
		double electricityCount = 0;
		double electricityCount_sinks = plot.getElectricityCount() - 2;
		double waterCount_negative = -2.5;
		double waterCount = 0;
		double waterCount_sinks = plot.getWaterCount() - 2;

		assertThrows(IllegalArgumentException.class, () -> {
			plot.setStatus(status);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setSize(size_negative);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setSize(size);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setDescription(description);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setEstimator(estimator_negative);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setEstimator(estimator);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setElectricityCountDifference(electricityCount_negative);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setElectricityCountDifference(electricityCount_sinks);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setElectricityCountDifference(electricityCount);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setElectricityCount(electricityCount_negative);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setWaterCountDifference(waterCount_negative);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setWaterCountDifference(waterCount_sinks);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setWaterCountDifference(waterCount);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setWaterCount(waterCount_negative);
		});
	}
}
