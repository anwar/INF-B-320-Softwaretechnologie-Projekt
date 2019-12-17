package kleingarten.plot;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.MonetaryAmount;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.salespointframework.core.Currencies.EURO;

public class PlotTests {
	private Plot plot;

	/**
	 * Setup of a mock of type {@link Plot} to run the tests with
	 */
	@BeforeEach
	public void setUp() {
		plot = new Plot("123", 500, "test");
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void initialStatusTest() {
		assertThat(plot.getStatus()).isEqualTo(PlotStatus.FREE);
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void initialSizeTest() {
		assertThat(plot.getSize()).isEqualTo(500);
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void initialDescriptionTest() {
		assertThat(plot.getDescription()).isEqualTo("test");
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void initalEstimatorTest() {
		assertThat(plot.getEstimator()).isEqualTo(Money.of(0, EURO));
	}

	/**
	 * Test if changes of the {@link Plot} are made correctly
	 */
	@Test
	public void changeStatusTest() {
		plot.setStatus(PlotStatus.TAKEN);
		assertThat(plot.getStatus()).isEqualTo(PlotStatus.TAKEN);
	}

	/**
	 * Test if changes of the {@link Plot} are made correctly
	 */
	@Test
	public void changeSizeTest() {
		plot.setSize(300);
		assertThat(plot.getSize()).isEqualTo(300);
	}

	/**
	 * Test if changes of the {@link Plot} are made correctly
	 */
	@Test
	public void changeDescriptionTest() {
		plot.setDescription("This plot is rented");
		assertThat(plot.getDescription()).isEqualTo("This plot is rented");
	}

	/**
	 * Test if changes of the {@link Plot} are made correctly
	 */
	@Test
	public void changeEstimatorTest() {
		plot.setEstimator(Money.of(400, EURO));
		assertThat(plot.getEstimator()).isEqualTo(Money.of(400, EURO));
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void illegalStatusTest() {
		PlotStatus status = null;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setStatus(status);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void negativeSizeTest() {
		int size_negative = -5;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setSize(size_negative);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void illegalSizeTest() {
		int size = 0;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setSize(size);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void illegalDescriptionTest() {
		String description = null;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setDescription(description);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void negativeEstimatorTest() {
		MonetaryAmount estimator_negative = Money.of(-5, EURO);
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setEstimator(estimator_negative);
		});
	}

	/**
	 * Test if illegal value for the {@link Plot}s attribute lead to exception of type {@link IllegalArgumentException}
	 */
	@Test
	public void illegalEstimatorTest() {
		MonetaryAmount estimator = null;
		assertThrows(IllegalArgumentException.class, () -> {
			plot.setEstimator(estimator);
		});
	}
}
