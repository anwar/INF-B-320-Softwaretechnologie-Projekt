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
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


/**
 * Unit tests for {@link Complaint}.
 */
@SpringBootTest
@Transactional
public class ComplaintTests {
	private DataService plotDataService;
	private Complaint complaint;
	private TenantManager tenantManager;

	public ComplaintTests(@Autowired TenantManager tenantManager,
						  @Autowired DataService plotDataService) {

		this.tenantManager = tenantManager;
		this.plotDataService = plotDataService;

		List<Tenant> tenants = tenantManager.findByRole(Role.of("Hauptpächter"));
		Tenant complainant = tenants.get(0);
		Plot plot = plotDataService.getRentedPlots(tenants.get(2)).iterator().next();

		this.complaint = new Complaint(plot, complainant, "subject for complaint",
				"description for complaint", ComplaintState.PENDING);
	}

	/**
	 * Tests for getters and setters of {@link Complaint}.
	 */
	@Test
	void getId() {
		assertThat(complaint.getId()).isNotNull();
	}

	@Test
	void getPlot() {
		assertThat(complaint.getPlot()).isNotNull();
	}

	@Test
	void getComplainant() {
		assertThat(complaint.getComplainant()).isNotNull();
	}

	@Test
	void getSubject() {
		assertThat(complaint.getSubject()).isNotEmpty();
	}

	@Test
	void setSubject() {
		String newSubject = "new subject";
		complaint.setSubject(newSubject);

		assertThat(complaint.getSubject()).isEqualTo(newSubject);
	}

	@Test
	void rejectEmptySubject() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> complaint.setSubject(""));
	}

	@Test
	void getDescription() {
		assertThat(complaint.getDescription()).isNotEmpty();
	}

	@Test
	void setDescription() {
		String newDescription = "new description";
		complaint.setDescription(newDescription);

		assertThat(complaint.getDescription()).isEqualTo(newDescription);
	}

	@Test
	void rejectEmptyDescription() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> complaint.setDescription(""));
	}

	@Test
	void getState() {
		assertThat(complaint.getState()).isNotNull();
	}

	@Test
	void setState() {
		ComplaintState newState = ComplaintState.FINISHED;
		complaint.setState(newState);

		assertThat(complaint.getState()).isEqualTo(newState);
	}

	@Test
	void rejectEmptyState() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> complaint.setState(null));
	}

	@Test
	void getAssignedObmann() {
		assertThat(complaint.getAssignedObmann()).isNotNull();
	}

	@Test
	void rejectEmptyAssignedObmann() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> complaint.setAssignedObmann(null));
	}

	@Test
	void setAssignedObmann() {
		Tenant newObmann = tenantManager.findByRole(Role.of("Obmann")).iterator().next();
		complaint.setAssignedObmann(newObmann);

		assertThat(complaint.getAssignedObmann()).isEqualTo(newObmann);
	}
}
