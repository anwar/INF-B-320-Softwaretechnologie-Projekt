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

import kleingarten.finance.Procedure;
import kleingarten.plot.DataService;
import kleingarten.plot.Plot;
import kleingarten.plot.PlotService;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A controller to handle web requests to manage {@link Complaint}s.
 */
@Controller
public class ComplaintController {
	private ComplaintManager complaintManager;
	private TenantManager tenantManager;
	private PlotService plotService;
	private DataService plotDataService;

	/**
	 * Constructor of {@link ComplaintController}
	 * @param complaintManager must not be {@literal null}
	 * @param tenantManager    must not be {@literal null}
	 * @param plotService      must not be {@literal null}
	 * @param plotDataService  must not be {@literal null}
	 */
	ComplaintController(ComplaintManager complaintManager,
						TenantManager tenantManager,
						PlotService plotService,
						DataService plotDataService) {

		Assert.notNull(complaintManager, "complaintManager must not be null!");
		Assert.notNull(tenantManager, "tenantManager must not be null!");
		Assert.notNull(plotService, "plotService must not be null!");
		Assert.notNull(plotDataService, "plotDataService must not be null!");

		this.complaintManager = complaintManager;
		this.tenantManager = tenantManager;
		this.plotService = plotService;
		this.plotDataService = plotDataService;
	}

	/**
	 * Controller for the view of the {@link Complaint}s
	 * @param user logged in user as {@link UserAccount}
	 * @param model of type {@link Model}
	 * @return html as {@link String}
	 */
	@PreAuthorize("hasRole('Hauptpächter') || hasRole('Nebenpächter')")
	@GetMapping("/complaints")
	String complains(@LoggedIn Optional<UserAccount> user, Model model) {
		if (user.isEmpty()) {
			return "redirect:/login";
		}

		Tenant tenant = tenantManager.getTenantByUserAccount(user.get());

		List<Complaint> complaints = null;
		if (tenant.hasRole("Vorstandsvorsitzender")) {
			complaints = complaintManager.getAll().toList();
		} else if (tenant.hasRole("Obmann")) {
			complaints = complaintManager.getForObmann(tenant);
		} else {
			complaints = complaintManager.getForComplainant(tenant).toList();
		}

		model.addAttribute("complaints", complaints);
		return "complaint/complaints";
	}

	/**
	 * View for creating a new {@link Complaint}
	 * @param plotId of the {@link Plot} of which {@link Tenant} authors the {@link Complaint}
	 * @param model as {@link Model}
	 * @return html as {@link String}
	 */
	@PreAuthorize("hasRole('Hauptpächter') || hasRole('Nebenpächter')")
	@GetMapping("/add-complaint/{plot_id}")
	String showComplaintCreationForm(@PathVariable("plot_id") String plotId, Model model) {
		String plotTenants = getPlotTenantNames(plotService.findById(plotId));

		model.addAttribute("plotId", plotId);
		model.addAttribute("plotTenants", plotTenants);
		return "complaint/addComplaint";
	}

	/**
	 * saves the new {@link Complaint}
	 * @param user logged in user as {@link UserAccount}
	 * @param plotId of the {@link Plot} of which {@link Tenant} authors the {@link Complaint}
	 * @param subject of the {@link Complaint} as {@link String}
	 * @param description of the {@link Complaint} as {@link String}
	 * @return html as {@link String}
	 */
	@PreAuthorize("hasRole('Hauptpächter') || hasRole('Nebenpächter')")
	@PostMapping("/save-complaint/{plot_id}")
	String saveComplaint(@LoggedIn Optional<UserAccount> user,
						 @PathVariable("plot_id") String plotId,
						 @RequestParam("subject") String subject,
						 @RequestParam("description") String description) {

		if (user.isEmpty()) {
			return "redirect:/login";
		}

		Plot plot = plotService.findById(plotId);
		Tenant complainant = tenantManager.getTenantByUserAccount(user.get());

		complaintManager.save(new Complaint(plot, complainant, subject, description, ComplaintState.PENDING));
		return "redirect:/complaints";
	}

	/**
	 * Form to update a {@link Complaint}
	 * @param id  identifier of the {@link Complaint}
	 * @param model as {@link Model}
	 * @return html as {@link String}
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	@PostMapping("/edit-complaint/{id}")
	String showComplaintUpdateForm(@PathVariable("id") long id, Model model) {
		Complaint complaint = complaintManager.get(id);
		String plotTenants = getPlotTenantNames(complaint.getPlot());
		List<Tenant> obmannList = tenantManager.findByRole(Role.of("Obmann"));

		model.addAttribute("complaint", complaintManager.get(id));
		model.addAttribute("plotTenants", plotTenants);
		model.addAttribute("obmannList", obmannList);
		return "complaint/editComplaint";
	}

	/**
	 * Updated {@link Complaint}
	 * @param id identifier of the {@link Complaint} as {@link Long}
	 * @param subject of the {@link Complaint} as {@link String}
	 * @param description of the {@link Complaint} as {@link String}
	 * @return html as {@link String}
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	@PostMapping("/update-complaint/{id}")
	String updateComplaint(@PathVariable("id") long id,
						   @RequestParam("subject") String subject,
						   @RequestParam("description") String description) {

		Complaint complaint = complaintManager.get(id);
		complaint.setSubject(subject);
		complaint.setDescription(description);

		complaintManager.save(complaint);
		return "redirect:/complaints";
	}

	/**
	 * Changes the state of a {@link Complaint}
	 * @param id identifier of {@link Complaint} as {@link Long}
	 * @return html as {@link String}
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender') || hasRole('Obmann')")
	@PostMapping("/change-complaint-state/{id}")
	String changeState(@PathVariable("id") long id) {
		Complaint complaint = complaintManager.get(id);
		ComplaintState currentState = complaint.getState();

		if (currentState == ComplaintState.PENDING) {
			complaint.setState(ComplaintState.FINISHED);
		} else {
			complaint.setState(ComplaintState.PENDING);
		}

		complaintManager.save(complaint);
		return "redirect:/complaints";
	}

	/**
	 * Changes the assigned obmann of a {@link Complaint}
	 * @param id identifier of the {@link Complaint} as {@link Long}
	 * @param obmannId identifier of the new obmann as {@link Long}
	 * @return html as {@link String}
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@GetMapping("/change-assigned-obmann/{id}/{obmann_id}")
	String changeAssignedObmann(@PathVariable("id") long id,
								@PathVariable("obmann_id") Long obmannId) {

		Complaint complaint = complaintManager.get(id);
		Tenant newAssignedObmann = plotDataService.findTenantById(obmannId);

		complaint.setAssignedObmann(newAssignedObmann);
		complaintManager.save(complaint);
		return "redirect:/complaints";
	}

	/**
	 * Deletes a {@link Complaint}
	 * @param id identifier of a {@link Complaint} as {@link Long}
	 * @return html as {@link String}
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@PostMapping("/delete-complaint/{id}")
	String deleteComplaint(@PathVariable("id") long id) {
		complaintManager.delete(id);
		return "redirect:/complaints";
	}

	/**
	 * Getter for the name of a {@link Tenant} of a {@link Plot}
	 * @param plot as {@link Plot}
	 * @return name of the {@link Tenant} as {@link String}
	 */
	private String getPlotTenantNames(Plot plot) {
		Procedure plotProcedure = plotDataService.getProcedure(plot);
		StringBuilder plotTenants = new StringBuilder().
				append(plotDataService.findTenantById(plotProcedure.getMainTenant()).getFullName());

		Set<Long> subTenantIds = plotDataService.getProcedure(plot).getSubTenants();
		if (!subTenantIds.isEmpty()) {
			for (Long id : subTenantIds) {
				String name = plotDataService.findTenantById(id).getFullName();
				plotTenants.append(", ").append(name);
			}
		}

		return plotTenants.toString();
	}
}
