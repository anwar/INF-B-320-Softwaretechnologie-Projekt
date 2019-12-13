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

import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ComplaintManager {

	private final ComplaintRepository complaints;
	private final TenantManager tenantManager;

	/**
	 * @param complaints    must not be {@literal null}
	 * @param tenantManager must not be {@literal null}
	 */
	@Autowired
	public ComplaintManager(ComplaintRepository complaints, TenantManager tenantManager) {
		Assert.notNull(complaints, "complaints must not be null!");
		Assert.notNull(tenantManager, "tenantManager must not be null!");

		this.complaints = complaints;
		this.tenantManager = tenantManager;
	}

	public Streamable<Complaint> getAll() {
		return complaints.findAll();
	}

	public Complaint get(long id) {
		return complaints.findById(id).get();
	}

	public Streamable<Complaint> getPending() {
		return complaints.findAll().filter(c -> c.getState().equals(ComplaintState.PENDING));
	}

	public Streamable<Complaint> getFinished() {
		return complaints.findAll().filter(c -> c.getState().equals(ComplaintState.FINISHED));
	}

	public String getComplainantName(Complaint complaint) {
		Tenant tenant = tenantManager.get(complaint.getComplainant().getId());
		return tenant.getFullName();
	}

	public String getAssignedObmannName(Complaint complaint) {
		Tenant tenant = tenantManager.get(complaint.getAssignedObmann().getId());
		return tenant.getFullName();
	}
}
