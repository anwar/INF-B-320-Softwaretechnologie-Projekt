package kleingarten.plot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InsecurePlotControllerTests {
	@Autowired
	MockMvc mvc;

	/**
	 * Test if a user who is not logged in can access the plot overview page; P-I-050
	 *
	 * @throws Exception
	 */
	@Test
	public void showPlotOverviewTest() throws Exception {
		mvc.perform(get("/anlage"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plotList", "plotColors", "colors", "userRights"))
				.andExpect(model().attributeDoesNotExist("canAdd"))
				.andExpect(view().name("plot/plotOverview"));
	}

}
