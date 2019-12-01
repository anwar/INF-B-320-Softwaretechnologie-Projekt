package kleingarten.plot;

import kleingarten.Finance.Procedure;
import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PlotServiceIntegrationTests {
	private Plot plot;
	private PlotService plotService;
	private PlotCatalog plotCatalog;
	private ProcedureManager procedureManager;
	private TenantManager tenantManager;

	public PlotServiceIntegrationTests(@Autowired PlotService plotService, @Autowired PlotCatalog plotCatalog, @Autowired ProcedureManager procedureManager, @Autowired TenantManager tenantManager) {
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.procedureManager = procedureManager;
		this.tenantManager = tenantManager;
	}

	@BeforeEach
	public void setUp() {
		this.plot = this.plotService.addNewPlot("123", 500, "test");
	}

	@Test
	public void addPlotTest() {
		Plot testPlot = plotService.addNewPlot("40", 200, "test");
		assertThat(plotCatalog.findById(testPlot.getId()).get()).isEqualTo(testPlot);
	}

	@Test
	public void getProcedureTest() {
		Tenant tenant = this.tenantManager.getAll().toList().get(0);
		Procedure procedure = procedureManager.add(new Procedure(2019, this.plot, tenant.getId()));
		assertThat(this.plotService.getProcedure(2019, this.plot.getId())).isEqualTo(procedure);
	}
}
