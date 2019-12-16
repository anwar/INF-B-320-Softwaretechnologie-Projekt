package kleingarten.plot;

import kleingarten.finance.Procedure;
import kleingarten.finance.ProcedureManager;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class with test cases for the methods implemented in {@link DataService}
 */
@SpringBootTest
@Transactional
public class DataServiceTests {
	private Plot firstPlot;
	private Plot secondPlot;
	private Procedure firstProcedure;
	private Procedure secondProcedure;
	private Tenant tenant;

	private final DataService dataService;
	private final PlotService plotService;
	private final UserAccountManager userAccountManager;
	private final ProcedureManager procedureManager;
	private final TenantRepository tenantRepository;

	/**
	 * Constructor of class, used by Spring
	 *
	 * @param dataService        {@link DataService} class which should be set as class attribute
	 * @param plotService        {@link PlotService} class which should be set as class attribute
	 * @param procedureManager   {@link ProcedureManager} class which should be set as class attribute
	 * @param userAccountManager {@link UserAccountManager} class which should be set as class attribute
	 * @param tenantRepository   {@link TenantRepository} class which should be set as class attribute
	 */
	public DataServiceTests(@Autowired DataService dataService, @Autowired PlotService plotService,
							@Autowired ProcedureManager procedureManager,
							@Autowired UserAccountManager userAccountManager,
							@Autowired TenantRepository tenantRepository) {
		this.dataService = dataService;
		this.plotService = plotService;
		this.userAccountManager = userAccountManager;
		this.procedureManager = procedureManager;
		this.tenantRepository = tenantRepository;
	}

	/**
	 * Add some mock objects to the repositories to provide information, which is needed by the test methods
	 */
	@BeforeEach
	public void setUp() {
		this.firstPlot = plotService.addNewPlot("123", 500, "test");
		this.secondPlot = plotService.addNewPlot("33", 40, "little plot");

		this.tenant = new Tenant("Max", "Mustermann", "Am Berg 5, 12423 Irgendwo im Nirgendwo",
				"01242354356", "13.04.1999",
				userAccountManager.create("max.mustermann", Password.UnencryptedPassword.of("123"),
						"max.mustermann@email.com", Role.of("Hauptpächter")));
		tenantRepository.save(tenant);
	}

	/**
	 * Positive test for searching for a specific {@link Procedure} for the saved {@link Plot}s
	 */
	@Test
	public void getProcedureTest() {
		this.firstProcedure = procedureManager.add(new Procedure(2019, firstPlot, tenant.getId()));
		this.secondProcedure = procedureManager.add(new Procedure(2019, secondPlot, tenant.getId()));
		assertThat(dataService.getProcedure(firstPlot)).isEqualTo(firstProcedure);
		assertThat(dataService.getProcedure(secondPlot)).isEqualTo(secondProcedure);
	}

	/**
	 * Negative test for searching for a specific {@link Procedure} for the saved {@link Plot}s
	 */
	@Test
	public void noProcedureTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			dataService.getProcedure(new Plot("890", 40, "test"));
		});
		assertThrows(IllegalArgumentException.class, () -> {
			dataService.getProcedure(new Plot("3000", 300, "test"));
		});
	}

	/**
	 * Test if there are saved {@link Procedure}s for the saved {@link Plot}s
	 */
	@Test
	public void procedureExistsTest() {
		this.firstProcedure = procedureManager.add(new Procedure(2019, firstPlot, tenant.getId()));
		this.secondProcedure = procedureManager.add(new Procedure(2019, secondPlot, tenant.getId()));
		assertThat(dataService.procedureExists(firstPlot)).isEqualTo(true);
		assertThat(dataService.procedureExists(secondPlot)).isEqualTo(true);
	}

	/**
	 * Negative test for checking if there is saved a {@link Procedure} for a {@link Plot}
	 */
	@Test
	public void procedureExistsNotTest() {
		assertThat(dataService.procedureExists(new Plot("80", 30, "test"))).isEqualTo(false);
	}

	/**
	 * Test if method for getting all rented {@link Plot}s for a specific {@link Tenant} works correctly
	 */
	@Test
	public void getRentedPlotsTest() {
		this.firstProcedure = procedureManager.add(new Procedure(2019, firstPlot, tenant.getId()));
		this.secondProcedure = procedureManager.add(new Procedure(2019, secondPlot, tenant.getId()));
		assertThat(dataService.getRentedPlots(tenant)).isEqualTo(Set.of(firstPlot, secondPlot));
	}

	/**
	 * Negative test for getting all rented {@link Plot}s for a specific {@link Tenant}
	 */
	@Test
	public void noPlotsRentedTest() {
		assertThat(dataService.getRentedPlots(tenant)).isEqualTo(new HashSet<>());
	}

	/**
	 * Test for getting a {@link Tenant} by his id
	 */
	@Test
	public void getTenantTest() {
		assertThat(dataService.findTenantById(tenant.getId())).isEqualTo(tenant);
	}

	/**
	 * Negative test for finding a {@link Tenant} by a provided id of type long
	 */
	@Test
	public void illegalTenantTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			dataService.findTenantById(300);
		});
	}
}
