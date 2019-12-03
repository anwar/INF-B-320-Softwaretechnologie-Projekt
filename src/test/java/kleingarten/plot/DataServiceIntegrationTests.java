package kleingarten.plot;

import kleingarten.Finance.Procedure;
import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class DataServiceIntegrationTests {
	private Plot plot;
	private final DataService dataService;
	private final PlotService plotService;
	private final TenantManager tenantManager;
	private final ProcedureManager procedureManager;

	public DataServiceIntegrationTests(@Autowired DataService dataService, @Autowired PlotService plotService, @Autowired TenantManager tenantManager, @Autowired ProcedureManager procedureManager) {
		this.dataService = dataService;
		this.plotService = plotService;
		this.tenantManager = tenantManager;
		this.procedureManager = procedureManager;
	}

	@BeforeEach
	public void setUp() {
		this.plot = plotService.addNewPlot("123", 500, "test");
	}

	@Test
	public void getProcedureTest() {
		Tenant tenant = tenantManager.getAll().toList().get(0);
		Procedure procedure = procedureManager.add(new Procedure(2019, this.plot, tenant.getId()));
		assertThat(dataService.getProcedure(2019, this.plot)).isEqualTo(procedure);
	}
}
