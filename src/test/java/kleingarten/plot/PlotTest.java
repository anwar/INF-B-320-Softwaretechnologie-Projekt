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
	 * Setup of a mock of type {@link Plot} to run the tests with
	 */
	@BeforeEach
	public void SetUp() {
		plot = new Plot("123", Money.of(300, EURO), 500, "test");
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void InitialStatusTest() {
		assertThat(plot.getStatus()).isEqualTo(PlotStatus.FREE);
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void InitialSizeTest() {
		assertThat(plot.getSize()).isEqualTo(500);
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void InitialDescriptionTest() {
		assertThat(plot.getDescription()).isEqualTo("test");
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void InitalEstimatorTest() {
		assertThat(plot.getEstimator()).isEqualTo(Money.of(300, EURO));
	}

	/**
	 * Test if changes of the {@link Plot} are made correctly
	 */
	@Test
	public void ChangeStatusTest() {
		plot.setStatus(PlotStatus.TAKEN);
		assertThat(plot.getStatus()).isEqualTo(PlotStatus.TAKEN);
	}

	/**
	 * Test if changes of the {@link Plot} are made correctly
	 */
	@Test
	public void ChangeSizeTest() {
		plot.setSize(300);
		assertThat(plot.getSize()).isEqualTo(300);
	}

	/**
	 * Test if changes of the {@link Plot} are made correctly
	 */
	@Test
	public void ChangeDescriptionTest() {
		plot.setDescription("This plot is rented");
		assertThat(plot.getDescription()).isEqualTo("This plot is rented");
	}

	/**
	 * Test if changes of the {@link Plot} are made correctly
	 */
	@Test
	public void ChangeEstimatorTest() {
		plot.setEstimator(Money.of(400, EURO));
		assertThat(plot.getEstimator()).isEqualTo(Money.of(400, EURO));
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void IllegalStatusTest() {
		PlotStatus status = null;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setStatus(status);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void NegativeSizeTest() {
		int size_negative = -5;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setSize(size_negative);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void IllegalSizeTest() {
		int size = 0;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setSize(size);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void IllegalDescriptionTest() {
		String description = null;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setDescription(description);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void NegativeEstimatorTest() {
		MonetaryAmount estimator_negative = Money.of(-5, EURO);
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setEstimator(estimator_negative);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void IllegalEstimatorTest() {
		MonetaryAmount estimator = null;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setEstimator(estimator);
		});
	}
}
