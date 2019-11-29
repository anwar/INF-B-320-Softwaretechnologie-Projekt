package kleingarten.plot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class PlotServiceIntegrationTests {
	private PlotService plotService;
	private PlotCatalog plotCatalog;

	public PlotServiceIntegrationTests(@Autowired PlotService plotService, @Autowired PlotCatalog plotCatalog) {
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
	}

	@Test
	public void nameExistsTest() {
		assertThat(plotService.existsByName("2")).isEqualTo(true);
	}

	@Test
	public void nameExistsNotTest() {
		assertThat(plotService.existsByName("33")).isEqualTo(false);
	}

	@Test
	public void illegalPlotNameTest() {
		assertThrows(IllegalArgumentException.class, () -> plotService.addNewPlot("2", 200, "test"));
	}

	@Test
	public void addPlotTest() {
		Plot plot = plotService.addNewPlot("40", 200, "test");
		assertThat(plotCatalog.findById(plot.getId()).get()).isEqualTo(plot);
	}
}
