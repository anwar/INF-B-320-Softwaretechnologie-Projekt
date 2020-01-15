package kleingarten.plot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.format.MonetaryFormats;
import java.util.HashMap;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class PlotInformationBufferTests {
	private Plot plot;
	private PlotInformationBuffer buffer;

	/**
	 * Setup of a mock of type {@link Plot} and the associated {@link PlotInformationBuffer} to run the tests with
	 */
	@BeforeEach
	public void setUp() {
		plot = new Plot("123", 500, "test");
		buffer = new PlotInformationBuffer(plot);
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void initialSetupTest() {
		assertThat(buffer.getPlotId()).isEqualTo(plot.getId());
		assertThat(buffer.getPlotName()).isEqualTo(plot.getName());
		assertThat(buffer.getPlotSize()).isEqualTo(plot.getSize() + " m²");
		assertThat(buffer.getPlotDescription()).isEqualTo(plot.getDescription());
		assertThat(buffer.getPlotPrice()).isEqualTo(MonetaryFormats.getAmountFormat(Locale.GERMANY).
				format(plot.getPrice()) + " Euro");

		assertThat(buffer.getMainTenantRoles()).isEqualTo(new HashMap<>());
		assertThat(buffer.getSubTenantRoles()).isEqualTo(new HashMap<>());
	}
}
