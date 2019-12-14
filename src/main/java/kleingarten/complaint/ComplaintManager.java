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
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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
		return complaints.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public void delete(long id) {
		Complaint complaint = get(id);
		complaints.delete(complaint);
	}

	public void save(Complaint complaint) {
		complaints.save(complaint);
	}

	public Streamable<Complaint> getForComplainant(Tenant complainant) {
		return complaints.findByComplainant(complainant, Sort.by("id"));
	}

	public List<Complaint> getForObmann(Tenant obmann) {
		List<Complaint> allComplaints = new ArrayList<>();

		// first we get the complaints that are assigned to Obmann
		Streamable<Complaint> complaintsAssignedToObmann = complaints.findByAssignedObmann(obmann, Sort.by("id"));
		if (!complaintsAssignedToObmann.isEmpty()) {
			for (Complaint c : complaintsAssignedToObmann) {
				allComplaints.add(c);
			}
		}

		// then we get all complaints that were made by Obmann
		Streamable<Complaint> complaintsByObmann = complaints.findByComplainant(obmann, Sort.by("id"));
		if (!complaintsByObmann.isEmpty()) {
			outerLoop:
			for (Complaint c : complaintsByObmann) {
				// only add it to the list if it doesn't exist already
				for (Complaint cInList : allComplaints) {
					if (cInList.getId() == c.getId()) {
						continue outerLoop; // to next complaint in complaintsByObmann
					}
				}
				allComplaints.add(c);
			}
		}

		return allComplaints;
	}
}
