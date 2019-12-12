package kleingarten.plot;

import kleingarten.finance.Procedure;
import kleingarten.finance.ProcedureManager;
import kleingarten.tenant.Tenant;
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

@SpringBootTest
@Transactional
public class DataServiceIntegrationTests {
	private Plot firstPlot;
	private Plot secondPlot;
	private Procedure firstProcedure;
	private Procedure secondProcedure;
	private Tenant tenant;

	private final DataService dataService;
	private final PlotService plotService;
	private final UserAccountManager userAccountManager;
	private final ProcedureManager procedureManager;

	/**
	 * Constructor of class, used by Spring
	 * @param dataService {@link DataService} class which should be set as class attribute
	 * @param plotService {@link PlotService} class which should be set as class attribute
	 * @param procedureManager {@link ProcedureManager} class which should be set as class attribute
	 * @param userAccountManager {@link UserAccountManager} class which should be set as class attribute
	 */
	public DataServiceIntegrationTests(@Autowired DataService dataService, @Autowired PlotService plotService,
									   @Autowired ProcedureManager procedureManager,
									   @Autowired UserAccountManager userAccountManager) {
		this.dataService = dataService;
		this.plotService = plotService;
		this.userAccountManager = userAccountManager;
		this.procedureManager = procedureManager;
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
		this.firstProcedure = procedureManager.add(new Procedure(2019, firstPlot, tenant.getId()));
		this.secondProcedure = procedureManager.add(new Procedure(2019, secondPlot, tenant.getId()));
	}

	/**
	 * Positive test for searching for a specific {@link Procedure} for the saved {@link Plot}s
	 */
	@Test
	public void getProcedureTest() {
		assertThat(dataService.getProcedure(firstPlot)).isEqualTo(firstProcedure);
		assertThat(dataService.getProcedure(secondPlot)).isEqualTo(secondProcedure);
	}

	/**
	 * Negative test for searching for a specific {@link Procedure} for the saved {@link Plot}s
	 */
	@Test
	public void noProcedureTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			dataService.getProcedure(firstPlot);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			dataService.getProcedure(secondPlot);
		});
	}

	/**
	 * Test if there are saved {@link Procedure} for a specific year for the saved {@link Plot}s
	 */
	@Test
	public void procedureExistsTest() {
		assertThat(dataService.procedureExists(firstPlot)).isEqualTo(true);
		assertThat(dataService.procedureExists(secondPlot)).isEqualTo(true);
		assertThat(dataService.procedureExists(firstPlot)).isEqualTo(false);
		assertThat(dataService.procedureExists(secondPlot)).isEqualTo(false);
	}

	/**
	 * Test if method for getting all rented {@link Plot}s for a specific {@link Tenant} and year works correctly
	 */
	@Test
	public void getRentedPlotsTest() {
		Set<Plot> rentedPlots = new HashSet<>();
		rentedPlots.add(firstPlot);
		rentedPlots.add(secondPlot);
		assertThat(dataService.getRentedPlots(tenant)).isEqualTo(rentedPlots);
		assertThat(dataService.getRentedPlots(tenant)).isEqualTo(Set.of());
	}
}
