package kleingarten.plot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;

import java.util.List;

public class UpdateChairmanFormTests {
	private List<ProductIdentifier> plots;

	/**
	 * Setup of a mock of type {@link UpdateChairmanForm} and the some {@link Plot}s to run the tests with
	 */
	@BeforeEach
	public void setUp() {
		Plot firstPlot = new Plot("123", 500, "test");
		Plot secondPlot = new Plot("3930", 79, "test");
		plots = List.of(firstPlot.getId(), secondPlot.getId());
	}

	/**
	 * Test if the initial setup has worked correctly
	 */
	@Test
	public void initialSetupTest() {
		assertThat(new UpdateChairmanForm(plots).getUpdatedPlots()).isEqualTo(plots);
	}

	/**
	 * Negative test for the setup
	 */
	@Test
	public void noPlotsChanged() {
		assertThat(new UpdateChairmanForm(List.of()).getUpdatedPlots()).isEqualTo(List.of());
	}
}
