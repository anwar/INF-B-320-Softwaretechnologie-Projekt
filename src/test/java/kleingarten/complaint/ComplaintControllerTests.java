package kleingarten.complaint;


import kleingarten.plot.PlotService;
import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ComplaintControllerTests {
	@Autowired
	MockMvc mvc;
	private PlotService plotService;
	private TenantManager tenantManager;
	private UserAccountManager userAccountManager;

	/**
	 * Test for the access of the complaints page
	 * @throws Exception if wrong
	 */
	@Test
	void complainsPreventPublicAccess() throws Exception {
		mvc.perform(get("/complaints"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	/**
	 * Test for the access of the complaints page
	 * @throws Exception if wrong
	 */
	@Test
	void complainsAccessibleForTenant() throws Exception {
		mvc.perform(get("/complaints").with(user("peter.klaus").roles("Hauptpächter")))
				.andExpect(status().isOk());
	}

}
