package kleingarten.plot;

import kleingarten.finance.Procedure;
import kleingarten.finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import kleingarten.tenant.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class with test cases for the methods implemented in {@link PlotControllerService}
 */
@SpringBootTest
@Transactional
public class PlotControllerServiceTests {
	private final PlotControllerService plotControllerService;
	private final DataService dataService;
	private final TenantManager tenantManager;
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;
	private final ProcedureManager procedureManager;
	private final UserAccountManager userAccountManager;
	private final TenantRepository tenantRepository;

	private Tenant boss = null;
	private Tenant replacement = null;
	private Tenant chairman = null;
	private Tenant chashier = null;
	private Tenant protocol = null;
	private Tenant waterman = null;

	private Plot freePlot;
	private Plot takenPlot;

	/**
	 * Constructor of class, used by Spring
	 *
	 * @param plotControllerService {@link PlotControllerService}
	 * @param dataService           {@link DataService} class which should be set as class attribute
	 * @param tenantManager         {@link TenantManager} class which should be set as class attribute
	 * @param plotService           {@link PlotService} class which should be set as class attribute
	 * @param plotCatalog           {@link PlotCatalog} class which should be set as class attribute
	 * @param procedureManager      {@link ProcedureManager} class which should be set as class attribute
	 * @param userAccountManager    {@link UserAccountManager} class which should be set as class attribute
	 * @param tenantRepository      {@link TenantRepository} class which should be set as class attribute
	 */
	public PlotControllerServiceTests(@Autowired PlotControllerService plotControllerService,
									  @Autowired DataService dataService,
									  @Autowired TenantManager tenantManager,
									  @Autowired PlotService plotService,
									  @Autowired PlotCatalog plotCatalog,
									  @Autowired ProcedureManager procedureManager,
									  @Autowired UserAccountManager userAccountManager,
									  @Autowired TenantRepository tenantRepository) {
		this.plotControllerService = plotControllerService;
		this.dataService = dataService;
		this.tenantManager = tenantManager;
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.procedureManager = procedureManager;
		this.userAccountManager = userAccountManager;
		this.tenantRepository = tenantRepository;
	}

	/**
	 * Add some mock objects to the repositories to provide information, which is needed by the test methods
	 */
	@BeforeEach
	public void setUp() {
		Set<Tenant> tenants = tenantManager.getAll().toSet();
		setUpImportantTenants(tenants);

		Set<Plot> plots = plotCatalog.findByStatus(PlotStatus.FREE);
		this.freePlot = setUpPlot(plots);
		plots = plotCatalog.findByStatus(PlotStatus.TAKEN);
		this.takenPlot = setUpPlot(plots);
	}

	/**
	 * Get and save the objects of type {@link Tenant} for all {@link Tenant}s with special roles as class attributes
	 *
	 * @param tenants initialized {@link Tenant}s as {@link Set} of {@link Tenant}
	 */
	public void setUpImportantTenants(Set<Tenant> tenants) {
		for (Tenant tenant :
				tenants) {
			if (tenantManager.tenantHasRole(tenant, Role.of("Vorstandsvorsitzender"))) {
				this.boss = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Stellvertreter"))) {
				this.replacement = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Obmann"))) {
				this.chairman = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Kassierer"))) {
				this.chashier = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Protokollant"))) {
				this.protocol = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Wassermann"))) {
				this.waterman = tenant;
			}
		}
	}

	/**
	 * Get a {@link Plot}
	 */
	public Plot setUpPlot(Set<Plot> plots) {
		if (plots.isEmpty()) {
			return new Plot("467", 400, "testPlot");
		}
		return plots.iterator().next();
	}


	/**
	 * Test if the right color is assigned to a free {@link Plot} if user is not logged in; P-U-030
	 */
	@Test
	public void insecureGetColorOfFreePlotTest() {
		assertThat(plotControllerService.setPlotColor(freePlot, Optional.empty())).isEqualTo(Map.of(freePlot, "#7CB342"));
	}

	/**
	 * Test if the right color is assigned to a free {@link Plot} if user is logged in; P-U-031
	 */
	@Test
	public void secureGetColorForFreePlotTest() {
		assertThat(plotControllerService.setPlotColor(freePlot, Optional.of(chairman.getUserAccount())))
				.isEqualTo(Map.of(freePlot, "#7CB342"));
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} when the user is not logged in;
	 * P-U-030
	 */
	@Test
	public void insecureGetColorForTakenPlotTest() {
		if (!dataService.procedureExists(takenPlot)) {
			Procedure takenPlotProcedure = procedureManager.add(new Procedure(2019, takenPlot, boss.getId()));
		}
		assertThat(plotControllerService.setPlotColor(takenPlot, Optional.empty())).isEqualTo(Map.of(takenPlot, "#546E7A"));
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} with
	 * {@link Role} "Vorstandsvorsitzender" when the user is logged in; P-U-031
	 */
	@Test
	public void securedGetColorForBossPlotTest() {
		Procedure procedure = procedureManager.getProcedures(2019, boss.getId()).toList().get(0);
		takenPlot = plotService.findById(procedure.getPlotId());
		assertThat(plotControllerService.setPlotColor(takenPlot, Optional.of(chairman.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, "#FDD835"));
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} with
	 * {@link Role} "Stellvertreter" when the user is logged in; P-U-031
	 */
	@Test
	public void securedGetColorForReplacementPlotTest() {
		Procedure procedure = new Procedure(2019, freePlot, replacement.getId());
		procedureManager.add(procedure);
		takenPlot = plotService.findById(procedure.getPlotId());
		assertThat(plotControllerService.setPlotColor(takenPlot, Optional.of(chairman.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, "#FDD835"));
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} with
	 * {@link Role} "Obmann" when the user is logged in; P-U-031
	 */
	@Test
	public void securedGetColorForChairmanPlotTest() {
		Procedure procedure = new Procedure(2019, freePlot, chairman.getId());
		procedureManager.add(procedure);
		takenPlot = plotService.findById(procedure.getPlotId());
		assertThat(plotControllerService.setPlotColor(takenPlot, Optional.of(chairman.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, "#039BE5"));

		Tenant tenant = tenantManager.findByRole(Role.of("Protokollant")).get(0);
		Procedure rentsAsSubtenant = procedureManager.add(new Procedure(2019, freePlot, tenant.getId()));
		rentsAsSubtenant.addSubTenant(chairman.getId());
		takenPlot = rentsAsSubtenant.getPlot();
		assertThat(plotControllerService.setPlotColor(takenPlot, Optional.of(chairman.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, "#039BE5"));
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} with
	 * {@link Role} "Obmann" and a {@link Tenant} with the {@link Role} "Vorstandsvorsitzender" when the user is logged in;
	 * P-U-031
	 */
	@Test
	public void securedGetColorForSpecialPlotTest() {
		Procedure procedure = procedureManager.getAll(boss.getId()).toList().get(0);
		procedure.addSubTenant(chairman.getId());
		procedureManager.add(procedure);
		takenPlot = plotService.findById(procedure.getPlotId());
		assertThat(plotControllerService.setPlotColor(takenPlot, Optional.of(chairman.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, "#E69138"));

		Procedure renting = procedureManager.getAll(chairman.getId()).toList().get(0);
		renting.addSubTenant(boss.getId());
		procedureManager.add(renting);
		takenPlot = plotService.findById(renting.getPlotId());
		assertThat(plotControllerService.setPlotColor(takenPlot, Optional.of(chairman.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, "#E69138"));
	}

	/**
	 * Test if access to the details page of a {@link Plot} is made correctly when no user is logged in; P-U-032
	 */
	@Test
	public void insecureAccessRightsForPlots() {
		assertThat(plotControllerService.setAccessRightForPlot(freePlot, Optional.empty()))
				.isEqualTo(Map.of(freePlot, true));
		assertThat(plotControllerService.setAccessRightForPlot(takenPlot, Optional.empty()))
				.isEqualTo(Map.of(takenPlot, false));
	}

	/**
	 * Test if access to the details page of a {@link Plot} is made correctly when a user with the {@link Role}
	 * "Vorstandsvorsitzender" or "Stellvertreter" is logged in; P-U-033
	 */
	@Test
	public void adminAccessRightsForPlots() {
		assertThat(plotControllerService.setAccessRightForPlot(freePlot, Optional.of(boss.getUserAccount())))
				.isEqualTo(Map.of(freePlot, true));
		assertThat(plotControllerService.setAccessRightForPlot(takenPlot, Optional.of(boss.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, true));
		assertThat(plotControllerService.setAccessRightForPlot(freePlot, Optional.of(replacement.getUserAccount())))
				.isEqualTo(Map.of(freePlot, true));
		assertThat(plotControllerService.setAccessRightForPlot(takenPlot, Optional.of(replacement.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, true));
	}

	/**
	 * Test if access to the details page of a {@link Plot} is made correctly when a user with the {@link Role}
	 * "Obmann" is logged in; P-U-033
	 */
	@Test
	public void chairmanAccessRightsForPlots() {
		assertThat(plotControllerService.setAccessRightForPlot(freePlot, Optional.of(chairman.getUserAccount())))
				.isEqualTo(Map.of(freePlot, true));
		assertThat(plotControllerService.setAccessRightForPlot(takenPlot, Optional.of(chairman.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, true));
	}

	/**
	 * Test if access to the details page of a {@link Plot} is made correctly when a user with the {@link Role}
	 * "Kassierer" is logged in; P-U-033
	 */
	@Test
	public void chashierAccessRightsForPlots() {
		assertThat(plotControllerService.setAccessRightForPlot(freePlot, Optional.of(chashier.getUserAccount())))
				.isEqualTo(Map.of(freePlot, true));
		assertThat(plotControllerService.setAccessRightForPlot(takenPlot, Optional.of(chashier.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, true));
	}

	/**
	 * Test if access to the details page of a {@link Plot} is made correctly when a user with the {@link Role}
	 * "Protokollant" is logged in; P-U-033
	 */
	@Test
	public void protocolAccessRightsForPlots() {
		assertThat(plotControllerService.setAccessRightForPlot(freePlot, Optional.of(protocol.getUserAccount())))
				.isEqualTo(Map.of(freePlot, true));
		assertThat(plotControllerService.setAccessRightForPlot(takenPlot, Optional.of(protocol.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, true));
	}

	/**
	 * Test if access to the details page of a {@link Plot} is made correctly when a user with the {@link Role}
	 * "Wassermann" is logged in; P-U-033
	 */
	@Test
	public void watermanAccessRightsForPlots() {
		assertThat(plotControllerService.setAccessRightForPlot(freePlot, Optional.of(waterman.getUserAccount())))
				.isEqualTo(Map.of(freePlot, true));
		assertThat(plotControllerService.setAccessRightForPlot(takenPlot, Optional.of(waterman.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, true));
	}

	/**
	 * Test if access to the details page of a {@link Plot} is made correctly when a user with no special {@link Role}
	 * is logged in; P-U-033
	 */
	@Test
	public void tenantAccessRightsForPlots() {
		Tenant tenant = new Tenant("Max", "Mustermann", "Am Berg 5, 12423 Irgendwo im Nirgendwo",
				"01242354356", "13.04.1999",
				userAccountManager.create("max.mustermann", Password.UnencryptedPassword.of("123"),
						"max.mustermann@email.com", Role.of("Hauptpächter")));
		tenantRepository.save(tenant);
		assertThat(plotControllerService.setAccessRightForPlot(freePlot, Optional.of(tenant.getUserAccount())))
				.isEqualTo(Map.of(freePlot, true));
		assertThat(plotControllerService.setAccessRightForPlot(takenPlot, Optional.of(tenant.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, false));

		Procedure procedure = new Procedure(2019, freePlot, tenant.getId());
		procedureManager.add(procedure);
		takenPlot = plotService.findById(procedure.getPlotId());
		assertThat(plotControllerService.setAccessRightForPlot(takenPlot, Optional.of(tenant.getUserAccount())))
				.isEqualTo(Map.of(takenPlot, true));
	}

	/**
	 * Test if information of a free {@link Plot} is correctly added to the associated {@link PlotInformationBuffer};
	 * P-U-040
	 */
	@Test
	public void addInformationOfFreePlotTest() {
		PlotInformationBuffer bufferForFreePlot = plotControllerService
				.addInformationOfPlotToPlotInformationBuffer(Optional.empty(), freePlot);
		assertThat(bufferForFreePlot.getPlotId()).isEqualTo(freePlot.getId());
	}

	/**
	 * Test if information of a rented {@link Plot} is correctly added to the associated {@link PlotInformationBuffer};
	 * P-U-041
	 */
	@Test
	public void addInformationOfRentedPlotTest() {
		Procedure procedure = procedureManager.getAll(boss.getId()).toList().get(0);
		procedure.addSubTenant(chairman.getId());
		PlotInformationBuffer bufferForTakenPlot = plotControllerService
				.addInformationOfPlotToPlotInformationBuffer(Optional.of(procedure), procedure.getPlot());
		assertThat(bufferForTakenPlot.getPlotId()).isEqualTo(procedure.getPlotId());
		assertThat(bufferForTakenPlot.getMainTenantRoles()).isEqualTo(Map.of(boss, boss.getRoles()));
		assertThat(bufferForTakenPlot.getSubTenantRoles()).isEqualTo(Map.of(chairman, chairman.getRoles()));
	}
}
