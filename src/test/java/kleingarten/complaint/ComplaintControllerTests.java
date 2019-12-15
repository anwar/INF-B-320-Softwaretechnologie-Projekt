/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kleingarten.complaint;

import kleingarten.plot.DataService;
import kleingarten.plot.Plot;
import kleingarten.plot.PlotService;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link ComplaintController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ComplaintControllerTests {
	@Autowired
	MockMvc mvc;

	private ComplaintManager complaintManager;
	private PlotService plotService;
	private DataService plotDataService;
	private TenantManager tenantManager;
	private UserAccountManager userAccountManager;

	public ComplaintControllerTests(@Autowired ComplaintManager complaintManager,
									@Autowired PlotService plotService,
									@Autowired DataService plotDataService,
									@Autowired TenantManager tenantManager,
									@Autowired UserAccountManager userAccountManager) {

		this.complaintManager = complaintManager;
		this.plotService = plotService;
		this.plotDataService = plotDataService;
		this.tenantManager = tenantManager;
		this.userAccountManager = userAccountManager;
	}

	/**
	 * Test for the redirect of non-authorized users to login page.
	 *
	 * @throws Exception if wrong
	 */
	@Test
	void complaintsPreventPublicAccess() throws Exception {
		mvc.perform(get("/complaints"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	/**
	 * Test for the access of the {@link Complaint} listing page.
	 *
	 * @throws Exception if wrong
	 */
	@Test
	void complaintsAccessibleForTenant() throws Exception {
		mvc.perform(get("/complaints")
				.with(user("peter.klaus").roles("Hauptpächter")))
				.andExpect(status().isOk())
				.andExpect(view().name("complaint/complaints"));
	}

	/**
	 * Test for the access of the {@link Complaint} creation page.
	 *
	 * @throws Exception if wrong
	 */
	@Test
	void showComplaintCreationForm() throws Exception {
		Tenant tenant = tenantManager.findByRole(Role.of("Hauptpächter")).iterator().next();
		Plot plot = plotDataService.getRentedPlots(tenant).iterator().next();

		mvc.perform(get("/add-complaint/{plot_id}", plot.getId())
				.with(user("peter.klaus").roles("Hauptpächter")))
				.andExpect(status().isOk())
				.andExpect(view().name("complaint/addComplaint"));
	}

	/**
	 * Test that a {@link Complaint} is successfully created and saved.
	 *
	 * @throws Exception if wrong
	 */
	@Test
	void saveComplaint() throws Exception {
		long noOfComplaints = complaintManager.getAll().toList().size();
		Tenant tenant = tenantManager.findByRole(Role.of("Hauptpächter")).iterator().next();
		Plot plot = plotDataService.getRentedPlots(tenant).iterator().next();

		mvc.perform(post("/save-complaint/{plot_id}", plot.getId())
				.param("subject", "Subject for complaint")
				.param("description", "Description for complaint")
				.with(user("peter.klaus").roles("Hauptpächter")))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/complaints"));

		assertThat(complaintManager.getAll().toList().size()).isEqualTo(noOfComplaints + 1);
	}

	/**
	 * Test for the access of the {@link Complaint} editing page.
	 *
	 * @throws Exception if wrong
	 */
	@Test
	void showComplaintUpdateForm() throws Exception {
		Complaint complaint = complaintManager.getAll().iterator().next();

		mvc.perform(post("/edit-complaint/{id}", complaint.getId())
				.with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().isOk())
				.andExpect(view().name("complaint/editComplaint"));
	}

	/**
	 * Test that a {@link Complaint} is successfully updated.
	 *
	 * @throws Exception if wrong
	 */
	@Test
	void updateComplaint() throws Exception {
		long complaintId = complaintManager.getAll().iterator().next().getId();
		String newSubject = "New subject for the complaint";
		String newDescription = "New description for the complaint";

		mvc.perform(post("/update-complaint/{id}", complaintId)
				.param("subject", newSubject)
				.param("description", newDescription)
				.with(user("peter.klaus").roles("Vorstandsvorsitzender")))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/complaints"));

		Complaint updatedComplaint = complaintManager.get(complaintId);

		assertThat(updatedComplaint.getSubject()).isEqualTo(newSubject);
		assertThat(updatedComplaint.getDescription()).isEqualTo(newDescription);
	}
}
