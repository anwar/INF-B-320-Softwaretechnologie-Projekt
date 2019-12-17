package kleingarten.plot;


import kleingarten.finance.ProcedureManager;
import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class PlotServiceIntegrationTests {
	private PlotService plotService;
	private PlotCatalog plotCatalog;
	private ProcedureManager procedureManager;
	private TenantManager tenantManager;

	/**
	 * Constructor of class, used by Spring
	 *
	 * @param plotService      {@link PlotService} class which should be set as class attribute
	 * @param plotCatalog      {@link PlotCatalog} class which should be set as class attribute
	 * @param procedureManager {@link ProcedureManager} class which should be set as class attribute
	 * @param tenantManager    {@link TenantManager} class which should be set as class attribute
	 */
	public PlotServiceIntegrationTests(@Autowired PlotService plotService, @Autowired PlotCatalog plotCatalog,
									   @Autowired ProcedureManager procedureManager,
									   @Autowired TenantManager tenantManager) {
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.procedureManager = procedureManager;
		this.tenantManager = tenantManager;
	}

	/**
	 * Test if a {@link Plot} is correctly added to the {@link PlotCatalog}
	 */
	@Test
	public void addPlotTest() {
		Plot testPlot = plotService.addNewPlot("40", 200, "test");
		assertThat(plotCatalog.findById(testPlot.getId()).get()).isEqualTo(testPlot);
	}

	/**
	 * Test if an {@link IllegalArgumentException} is thrown when a new {@link Plot} which should be saved has the same
	 * name as {@link String} like a {@link Plot} which is saved in the {@link PlotCatalog}
	 */
	@Test
	public void illegalPlotNameTest() {
		assertThrows(IllegalArgumentException.class, () -> plotService.addNewPlot("2", 200, "test"));
	}

	/**
	 * Test if a {@link Plot} with the given name as {@link String} exists
	 */
	@Test
	public void nameExistsTest() {
		assertThat(plotService.existsByName("2")).isEqualTo(true);
	}

	/**
	 * Test if a {@link Plot} with the given name as {@link String} does not exist
	 */
	@Test
	public void nameExistsNotTest() {
		assertThat(plotService.existsByName("33")).isEqualTo(false);
	}

	/**
	 * Test if a {@link Plot} with the given {@link org.salespointframework.catalog.ProductIdentifier} exists in
	 * {@link PlotCatalog}
	 */
	@Test
	public void findPlotByIdTest() {
		Plot plot = plotService.addNewPlot("40", 500, "test");
		assertThat(plotService.findById(plot.getId())).isEqualTo(plot);
	}

	/**
	 * Test if an {@link IllegalArgumentException} is thrown when an {@link Plot} with an illegal
	 * {@link org.salespointframework.catalog.ProductIdentifier} should be found
	 */
	@Test
	public void illegalPlotIdTest() {
		Plot plot = new Plot("80", 3000, "test");
		assertThrows(IllegalArgumentException.class, () -> plotService.findById(plot.getId()));
	}

}
