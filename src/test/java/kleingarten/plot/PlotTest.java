package kleingarten.plot;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.salespointframework.core.Currencies.EURO;

public class PlotTest {
	private Plot plot;

	@BeforeEach
	public void setup() {
		plot = new Plot("123", Money.of(300, EURO), 500, "test", 5, 6);
	}

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
}
