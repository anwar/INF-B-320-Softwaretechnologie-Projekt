package kleingarten.plot;

import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurePlotControllerTests {
	@Autowired
	MockMvc mvc;

	@Autowired
	PlotService plotService;

	@Autowired
	PlotCatalog plotCatalog;

	@Autowired
	TenantManager tenantManager;

	/**
	 * Test if a logged in user with the {@link org.salespointframework.useraccount.Role} "Vorstandsvorsitzender" can
	 * change the {@link PlotStatus} of a {@link Plot}; P-U-020
	 *
	 * @throws Exception
	 */
	@Test
	public void cancelPlotIsAvailableForAdmin() throws Exception {
		Plot takenPlot = plotCatalog.findByStatus(PlotStatus.TAKEN).iterator().next();
		mvc.perform(get("/cancelPlot/" + takenPlot.getId())
				.with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/anlage"));

		assertThat(plotService.findById(takenPlot.getId()).getStatus()).isEqualTo(PlotStatus.FREE);
	}

	/**
	 * Test if a logged in user who has not the {@link org.salespointframework.useraccount.Role} "Vorstandsvorsitzender"
	 * can change the {@link PlotStatus} of a {@link Plot}; P-U-021
	 *
	 * @throws Exception
	 */
	@Test
	public void cancelPlotIsNotAvailableForUser() throws Exception {
		Plot takenPlot = plotCatalog.findByStatus(PlotStatus.TAKEN).iterator().next();
		mvc.perform(get("/cancelPlot/" + takenPlot.getId())
				.with(user("hubertgrumpel").roles("Obmann")))
				.andExpect(status().isForbidden());
	}

	/**
	 * Test if a user with the {@link org.salespointframework.useraccount.Role} "Vorstandsvorsitzender"
	 * or "Stellvertreter" can access the {@link Plot} overview page; P-I-051
	 *
	 * @throws Exception
	 */
	@Test
	public void showPlotOverviewForAdminTest() throws Exception {
		mvc.perform(get("/anlage")
				.with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plotList", "plotColors", "colors", "userRights", "canAdd"))
				.andExpect(view().name("plot/plotOverview"));

		mvc.perform(get("/anlage")
				.with(user("sophie").roles("Stellvertreter")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plotList", "plotColors", "colors", "userRights", "canAdd"))
				.andExpect(view().name("plot/plotOverview"));
	}

	/**
	 * Test if a logged in user can access the {@link Plot} overview page; P-I-051
	 *
	 * @throws Exception
	 */
	@Test
	public void showPlotOverviewForLoggedInUser() throws Exception {
		mvc.perform(get("/anlage")
				.with(user("hubertgrumpel").roles("Obmann")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plotList", "plotColors", "colors", "userRights"))
				.andExpect(model().attributeDoesNotExist("canAdd"))
				.andExpect(view().name("plot/plotOverview"));
	}

	/**
	 * Test if a logged in user can access the detail page of his rented {@link Plot}; P-U-043
	 *
	 * @throws Exception
	 */
	@Test
	public void showPlotsForUserTest() throws Exception {
		mvc.perform(get("/myPlot")
				.with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plots", "rented", "canSeeWorkhours", "canSeeBills",
						"canModify", "rents"))
				.andExpect(model().attribute("canSeeWorkhours", true))
				.andExpect(model().attribute("canSeeBills", true))
				.andExpect(model().attribute("canModify", true))
				.andExpect(model().attribute("rents", true))
				.andExpect(view().name("plot/myPlot"));
	}

	/**
	 * Test if logged in user with the {@link Role} "Vorstandsvorsitzender" or "Stellvertreter" can access all
	 * information of a {@link Plot}; P-U-042
	 *
	 * @throws Exception
	 */
	@Test
	public void detailsOfPlotForAdminTest() throws Exception {
		Plot takenPlot = plotCatalog.findByStatus(PlotStatus.TAKEN).iterator().next();
		mvc.perform(get("/plot/" + takenPlot.getId())
				.with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plots", "rented", "canSeeWorkhours", "canSeeBills",
						"canModify", "canSeeApplications", "canCancel"))
				.andExpect(model().attribute("canSeeWorkhours", true))
				.andExpect(model().attribute("canSeeBills", true))
				.andExpect(model().attribute("canModify", true))
				.andExpect(model().attribute("canSeeApplications", true))
				.andExpect(model().attribute("canCancel", true))
				.andExpect(view().name("plot/myPlot"));

		mvc.perform(get("/plot/" + takenPlot.getId())
				.with(user("sophie").roles("Stellvertreter")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plots", "rented", "canSeeWorkhours", "canSeeBills",
						"canModify", "canSeeApplications", "canCancel"))
				.andExpect(model().attribute("canSeeWorkhours", true))
				.andExpect(model().attribute("canSeeBills", true))
				.andExpect(model().attribute("canModify", true))
				.andExpect(model().attribute("canSeeApplications", true))
				.andExpect(model().attribute("canCancel", true))
				.andExpect(view().name("plot/myPlot"));
	}

	/**
	 * Test if logged in user with the {@link Role} "Kassierer" can access all information of a {@link Plot}; P-U-042
	 *
	 * @throws Exception
	 */
	@Test
	public void detailsOfPlotForChashierTest() throws Exception {
		Plot takenPlot = plotCatalog.findByStatus(PlotStatus.TAKEN).iterator().next();
		mvc.perform(get("/plot/" + takenPlot.getId())
				.with(user("bill").roles("Kassierer")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plots", "rented", "canSeeWorkhours", "canSeeBills"))
				.andExpect(model().attribute("canSeeWorkhours", true))
				.andExpect(model().attribute("canSeeBills", true))
				.andExpect(view().name("plot/myPlot"));
	}

	/**
	 * Test if logged in user with the {@link Role} "Wassermann" can access all information of a {@link Plot}; P-U-042
	 *
	 * @throws Exception
	 */
	@Test
	public void detailsOfPlotForWatermanTest() throws Exception {
		Plot takenPlot = plotCatalog.findByStatus(PlotStatus.TAKEN).iterator().next();
		mvc.perform(get("/plot/" + takenPlot.getId())
				.with(user("neptun").roles("Wassermann")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plots", "rented", "canModify"))
				.andExpect(model().attribute("canModify", true))
				.andExpect(view().name("plot/myPlot"));
	}

	/**
	 * Test if a logged in user with the {@link org.salespointframework.useraccount.Role} "Vorstandsvorsitzender" can
	 * add a new {@link Plot}; P-U-010
	 *
	 * @throws Exception
	 */
	@Test
	public void addNewPlotIsAvailableForAdminTest() throws Exception {
		showPlotOverviewForAdminTest();
		mvc.perform(get("/plotRegistration")
				.with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk())
				.andExpect(view().name("plot/addPlot"));

		mvc.perform(post("/addPlot")
				.with(user("peter.klaus").roles("Vorstandsvorsitzender"))
				.param("name", "300")
				.param("size", "500")
				.param("description", "test"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/chairmenOverview"));

		assertThat(plotService.existsByName("300")).isEqualTo(true);
	}

	/**
	 * Test if no duplicate of a existing {@link Plot} can be added
	 *
	 * @throws Exception
	 */
	@Test
	public void errorForAddingDuplicatedPlot() throws Exception {
		showPlotOverviewForAdminTest();
		mvc.perform(get("/plotRegistration")
				.with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk())
				.andExpect(view().name("plot/addPlot"));

		mvc.perform(post("/addPlot")
				.with(user("peter.klaus").roles("Vorstandsvorsitzender"))
				.param("name", "1")
				.param("size", "500")
				.param("description", "test"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Parzelle mit der gewählten Nummer existiert bereits!"))
				.andExpect(view().name("plot/addPlot"));
	}


	/**
	 * Test if a logged in user who has not the {@link org.salespointframework.useraccount.Role} "Vorstandsvorsitzender"
	 * can not add a new {@link Plot}; P-U-011
	 *
	 * @throws Exception
	 */
	@Test
	public void addNewPlotIsNotAvailableForUserTest() throws Exception {
		showPlotOverviewForLoggedInUser();
		mvc.perform(get("/plotRegistration")
				.with(user("hubertgrumpel").roles("Obmann")))
				.andExpect(status().isForbidden());
	}

	/**
	 * Test if redirect works when a user with the {@link org.salespointframework.useraccount.Role}
	 * "Vorstandsvorsitzender" tries to add {@link Plot}s for a not given chairman
	 *
	 * @throws Exception
	 */
	@Test
	public void addChairmanWithoutParamTest() throws Exception {
		mvc.perform(post("/addchairman")
				.with(user("peter.klaus").roles("Vorstandsvorsitzender"))
				.param("tenantID", ""))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/chairmenOverview"));
	}

	/**
	 * Test if a logged in user who has the {@link org.salespointframework.useraccount.Role} "Vorstandsvorsitzender"
	 * can not add {@link Plot}s for a chairman; P-U-050
	 *
	 * @throws Exception
	 */
	@Test
	public void addChairmanIsAvailableForAdminTest() throws Exception {
		Tenant chairman = tenantManager.findByRole(Role.of("Obmann")).iterator().next();
		mvc.perform(post("/addchairman")
				.with(user("peter.klaus").roles("Vorstandsvorsitzender"))
				.param("tenantID", String.valueOf(chairman.getId())))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("administratedPlots", "updateChairmanForm", "tenant",
						"canAddChairman"))
				.andExpect(view().name("plot/plotOverview"));
	}
}
