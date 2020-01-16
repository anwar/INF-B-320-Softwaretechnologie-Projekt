package kleingarten.tenant;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TenantControllerTest {
	@Autowired
	MockMvc mvc;

	@Autowired
	TenantController tenantController;




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
	void tenantDetailsIsAccessibleForManager() throws Exception {
		mvc.perform(get("/tenantDetails?id=10").with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk());
	}

	@Test
	void tenantDetailsPreventPublicAccess() throws Exception {
		mvc.perform(get("/tenantDetails?id=10"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	void modifyTenantIsAccessibleForManager() throws Exception {
		mvc.perform(get("/modifyTenant?id=10").with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk());
	}

	@Test
	void modifyTenantPreventPublicAccess() throws Exception {
		mvc.perform(get("/modifyTenant?id=10"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	void changePasswordPreventPublicAccsess()throws Exception{
		mvc.perform(get("/changePassword"))
			.andExpect(status().isFound())
			.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}


	@Test
	void preTenantIsAccessibleForManager() throws Exception{
		mvc.perform(get("/pretenants").with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk());
	}

	@Test
	void preTenantPreventPublicAccess() throws Exception{
		mvc.perform(get("/pretenants"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}
	@Test
	void registerIsAccessibleForManager() throws Exception{
		mvc.perform(get("/register").with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk());
	}

	@Test
	void registerPreventPublicAccess() throws Exception{
		mvc.perform(get("/register"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

}
