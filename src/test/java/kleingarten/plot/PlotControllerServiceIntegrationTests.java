package kleingarten.plot;

import kleingarten.Finance.Procedure;
import kleingarten.Finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;

import javax.money.format.MonetaryFormats;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@Transactional
public class PlotControllerServiceIntegrationTests {
	private Tenant boss = null;
	private Tenant replacement = null;
	private Tenant chairman = null;

	private Map<Plot, String> colors;
	private Map<Plot, String> result;

	private Plot freePlot;
	private Plot takenPlot;

	private final PlotControllerService plotControllerService;
	private final DataService dataService;
	private final TenantManager tenantManager;
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;
	private final ProcedureManager procedureManager;

	/**
	 * Constructor of class, used by Spring
	 *
	 * @param dataService   {@link DataService} class which should be set as class attribute
	 * @param tenantManager {@link TenantManager} class which should be set as class attribute
	 */
	public PlotControllerServiceIntegrationTests(@Autowired PlotControllerService plotControllerService,
												 @Autowired DataService dataService,
												 @Autowired TenantManager tenantManager,
												 @Autowired PlotService plotService,
												 @Autowired PlotCatalog plotCatalog,
												 @Autowired ProcedureManager procedureManager) {
		this.plotControllerService = plotControllerService;
		this.dataService = dataService;
		this.tenantManager = tenantManager;
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
		setUpImportantTenants(tenants);

		Set<Plot> plots = plotCatalog.findByStatus(PlotStatus.FREE);
		this.freePlot = setUpPlot(plots);
		plots = plotCatalog.findByStatus(PlotStatus.TAKEN);
		this.takenPlot = setUpPlot(plots);

		this.colors = new HashMap<>();
		this.result = new HashMap<>();
	}

	/**
	 * Get and save the objects of type {@link Tenant} for all {@link Tenant}s with special roles as class attributes
	 * @param tenants initialized {@link Tenant}s as {@link Set} of {@link Tenant}
	 */
	public void setUpImportantTenants(Set<Tenant> tenants) {
		for (Tenant tenant:
			tenants) {
			if (tenantManager.tenantHasRole(tenant, Role.of("Vorstandsvorsitzender"))) {
				this.boss = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Stellvertreter"))) {
				this.replacement = tenant;
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Obmann"))) {
				this.chairman = tenant;
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
	 * Test if the right color is assigned to a free {@link Plot} if user is not logged in
	 */
	@Test
	public void insecureGetColorOfFreePlotTest() {
		result.put(freePlot, "olive");

		plotControllerService.insecureSetPlotColor(freePlot, colors);
		assertThat(colors).isEqualTo(result);
	}

	/**
	 * Test if the right color is assigned to a free {@link Plot} if user is logged in
	 */
	@Test
	public void secureGetColorForFreePlotTest() {
		result.put(freePlot, "olive");

		plotControllerService.secureSetPlotColor(freePlot, colors);
		assertThat(colors).isEqualTo(result);
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} when the user is not logged in
	 */
	@Test
	public void insecureGetColorForTakenPlotTest() {
		if (!dataService.procedureExists(2019, takenPlot)) {
			Procedure takenPlotProcedure = procedureManager.add(new Procedure(2019, takenPlot, boss.getId()));
		}
		result.put(takenPlot, "grey");

		plotControllerService.insecureSetPlotColor(takenPlot, colors);
		assertThat(colors).isEqualTo(result);
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} with
	 * {@link Role} "Vorstandsvorsitzender" when the user is logged in
	 */
	@Test
	public void securedGetColorForBossPlotTest() {
		Procedure procedure = procedureManager.getProcedures(2019, boss.getId()).toList().get(0);
		takenPlot = plotService.findById(procedure.getPlotId());
		result.put(takenPlot, "yellow");

		plotControllerService.secureSetPlotColor(takenPlot, colors);
		assertThat(colors).isEqualTo(result);
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} with
	 * {@link Role} "Stellvertreter" when the user is logged in
	 */
	@Test
	public void securedGetColorForReplacementPlotTest() {
		Procedure procedure = new Procedure(2019,freePlot, replacement.getId());
		procedureManager.add(procedure);
		takenPlot = plotService.findById(procedure.getPlotId());
		result.put(takenPlot, "yellow");

		plotControllerService.secureSetPlotColor(takenPlot, colors);
		assertThat(colors).isEqualTo(result);
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} with
	 * {@link Role} "Obmann" when the user is logged in
	 */
	@Test
	public void securedGetColorForChairmanPlotTest() {
		Procedure procedure = new Procedure(2019,freePlot, chairman.getId());
		procedureManager.add(procedure);
		takenPlot = plotService.findById(procedure.getPlotId());
		result.put(takenPlot, "blue");

		plotControllerService.secureSetPlotColor(takenPlot, colors);
		assertThat(colors).isEqualTo(result);
	}

	/**
	 * Test if the right color is assigned to a {@link Plot} taken by a {@link Tenant} with
	 * {@link Role} "Obmann" and a {@link Tenant} with the {@link Role} "Vorstandsvorsitzender" when the user is logged in
	 */
	@Test
	public void securedGetColorForSpecialPlotTest() {
		Procedure procedure = procedureManager.getProcedures(2019, boss.getId()).toList().get(0);
		procedure.addSubTenant(chairman.getId());
		procedureManager.add(procedure);
		takenPlot = plotService.findById(procedure.getPlotId());
		result.put(takenPlot, "orange");

		plotControllerService.secureSetPlotColor(takenPlot, colors);
		assertThat(colors).isEqualTo(result);
	}

	@Test
	public void addInformationOfPlotTest() {
		ModelAndView mav = new ModelAndView();
		ModelAndView result = new ModelAndView();
		result.addObject("plot", freePlot);
		result.addObject("plotID", freePlot.getId());
		result.addObject("plotName", freePlot.getName());
		result.addObject("plotSize", freePlot.getSize() + " m²");
		result.addObject("plotDescription", freePlot.getDescription());
		result.addObject("plotPrice", MonetaryFormats.getAmountFormat(Locale.GERMANY)
				.format(freePlot.getEstimator()));

		plotControllerService.addGeneralInformationOfPlot(freePlot, mav);
		assertThat(mav.getModelMap()).isEqualTo(result.getModelMap());
	}
}
