package kleingarten.tenant;


import org.junit.jupiter.api.Test;
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
public class TenantControllerTest {
	@Autowired
	MockMvc mvc;

	@Test
	void tenantlistIsAccessibleForManager() throws Exception {
		mvc.perform(get("/tenants").with(user("peter.klaus").roles("Vorstandsvorsitzender")))
			.andExpect(status().isOk());

	}

	@Test
	void tenantlistPreventPublicAccess() throws Exception {

		mvc.perform(get("/tenants"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	void tenantDetailsIsAccessibleForManager() throws Exception{
		mvc.perform(get("/tenantDetails?id=5").with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk());
	}

	@Test
	void tenantDetailsPreventPublicAccess() throws Exception{
		mvc.perform(get("/tenantDetails?id=5"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	void modifyTenantIsAccessibleForManager() throws Exception{
		mvc.perform(get("/modifyTenant?id=5").with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk());
	}

	@Test
	void modifyTenantPreventPublicAccess() throws Exception{
		mvc.perform(get("/modifyTenant?id=5"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}


}
