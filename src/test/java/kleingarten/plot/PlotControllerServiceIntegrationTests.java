package kleingarten.plot;

import kleingarten.Finance.Procedure;
import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import javax.transaction.Transactional;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PlotControllerServiceIntegrationTests {
	private Tenant boss = null;
	private Tenant replacement = null;
	private Tenant chashier = null;
	private Tenant chairman = null;
	private Set<Plot> plots = new HashSet<>();
	private Plot plot;

	private Plot bossPlot;

	private final PlotControllerService plotControllerService;
	private final DataService dataService;
	private final TenantManager tenantManager;
	private final UserAccountManager userAccountManager;
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;
	private final ProcedureManager procedureManager;

	/**
	 * Constructor of class, used by Spring
	 * @param dataService {@link DataService} class which should be set as class attribute
	 * @param tenantManager {@link TenantManager} class which should be set as class attribute
	 */
	public PlotControllerServiceIntegrationTests(@Autowired PlotControllerService plotControllerService,
												 @Autowired DataService dataService,
												 @Autowired TenantManager tenantManager,
												 @Autowired UserAccountManager userAccountManager,
												 @Autowired PlotService plotService,
												 @Autowired PlotCatalog plotCatalog,
												 @Autowired ProcedureManager procedureManager) {
		this.plotControllerService = plotControllerService;
		this.dataService = dataService;
		this.tenantManager = tenantManager;
		this.userAccountManager = userAccountManager;
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.procedureManager = procedureManager;
	}

	/**
	 * Add some mock objects to the repositories to provide information, which is needed by the test methods
	 */
	@BeforeEach
	public void setUp() {
		Set<Tenant> tenants = tenantManager.getAll().toSet();
		for (Tenant tenant:
			 tenants) {
			if (tenantManager.tenantHasRole(tenant, Role.of("Vorstandsvorsitzender"))) {
				this.boss = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Stellvertreter"))) {
				this.replacement = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Kassierer"))) {
				this.chashier = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Obmann"))) {
				this.chairman = tenant;
			}
		}
		this.plots = plotCatalog.findByStatus(PlotStatus.FREE);
		for (Plot freePlot:
			 plots) {
			this.plot = freePlot;
			break;
		}
	}

	/**
	 * Test if the right color is assigned to a free {@link Plot} if user is not logged in
	 */
	@Test
	public void insecureGetColorOfFreePlotTest() {
		Map<Plot, String> colors = new HashMap<>();
		Map<Plot, String> result = new HashMap<>();
		Optional<UserAccount> user = Optional.empty();

		if (plot == null) {
			plot = new Plot("467", 400, "testPlot");
		}
		result.put(plot, "olive");

		plotControllerService.setPlotColor(plot, user, colors);
		assertThat(colors).isEqualTo(result);
	}

	/**
	 * Test if the right color is assigned to a free {@link Plot} if user is logged in
	 */
	@Test
	public void secureGetColorForFreePlotTest() {
		Map<Plot, String> colors = new HashMap<>();
		Map<Plot, String> result = new HashMap<>();
		Optional<UserAccount> user = Optional.of(boss.getUserAccount());

		if (plot == null) {
			plot = new Plot("467", 400, "testPlot");
		}
		result.put(plot, "olive");

		plotControllerService.setPlotColor(plot, user, colors);
		assertThat(colors).isEqualTo(result);
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} with {@link Role} "Vorstandsvorsitzender"
	 */
	@Test
	public void securedGetColorForBossPlotTest() {
		Map<Plot, String> colors = new HashMap<>();
		Map<Plot, String> result = new HashMap<>();
		Optional<UserAccount> user = Optional.of(boss.getUserAccount());

		if (dataService.getRentedPlots(2019, boss).isEmpty()) {
			bossPlot = new Plot("399", 300, "test");
			Procedure bossProcedure = new Procedure(2019, bossPlot, boss.getId());
		} else {
			for (Plot plot:
				 dataService.getRentedPlots(2019, boss)) {
				bossPlot = plot;
				break;
			}
		}
		result.put(bossPlot, "yellow");

		plotControllerService.setPlotColor(bossPlot, user, colors);
		assertThat(colors).isEqualTo(result);

	}
}
