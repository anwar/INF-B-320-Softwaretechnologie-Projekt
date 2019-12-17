package kleingarten.plot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurePlotControllerTests {
	@Autowired
	MockMvc mvc;

	//@Test
	//public void cancelPlotIsAvailableForManager() {
	//mvc.perform(get("/cancelPlot")).with(user("peter.klaus").roles("Vorstandsvorsitzender"))).param()
	//}

	/**
	 * Test if a user with the {@link org.salespointframework.useraccount.Role} "Vorstandsvorsitzender"
	 * or "Stellvertreter" can access the plot overview page; P-I-051
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
	 * Test if a logged in user can access the plot overview page; P-I-051
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

}
