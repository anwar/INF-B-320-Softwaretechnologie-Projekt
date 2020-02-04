// Copyright 2019-2020 the original author or authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package kleingarten.finance;

import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProcedureController {
	private ProcedureManager procedureManager;
	private TenantManager tenantManager;

	public ProcedureController(ProcedureManager procedureManager, TenantManager tenantManager) {
		this.procedureManager = procedureManager;
		this.tenantManager = tenantManager;
	}

	/**
	 * Provide simple Lists of procedure
	 *
	 * @param model for the view
	 * @return a view
	 */
	@GetMapping("/procedure")
	//@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String procedure(Model model) {
		model.addAttribute("procedureList", procedureManager.getAll());
		return "finance/procedure";
	}

	/**
	 * By clicking: able to see all the details of selected procedure.
	 *
	 * @param id    of the procedure
	 * @param model for the view
	 * @return a view
	 */
	@GetMapping("/procedureDetails")
	//@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String procedureDetails(@RequestParam("id") String id, Model model) {
		model.addAttribute("procedure", procedureManager.get(Long.parseLong(id)));
		return "finance/procedureDetails";
	}

	/**
	 * @param model for the view
	 * @return a view
	 */
	@GetMapping("/updateProcedure")
	String updateProcedure(Model model) {
		model.addAttribute("procedure", procedureManager.getAll());
		return "finance/updateProcedure";
	}

	/**
	 * Obmann will be able to change the value of watercount
	 *
	 * @param model  for the view
	 * @param plotId is the Id of the plot
	 * @return a view
	 */
	@GetMapping("/procedureDetails/editWatercount/{plotId}")
	//@PreAuthorize("hasRole('ROLE_VORSTAND')")
	public String editWatercount(Model model, @PathVariable String plotId) {
		model.addAttribute("procedure", procedureManager.findByPlotId(plotId).get());
		return "finance/editWatercount";
	}

	/**
	 * @param model       for the view
	 * @param procedureId is the Id of the procedure
	 * @param water       is the water count
	 * @return a redirect string
	 */
	@PostMapping("/procedureDetails/editWatercount")
	public String saveWatercount(Model model, @RequestParam("procedureId") String procedureId,
								 @RequestParam String water) {
		// those three lines are just for checking the value. Not necessary to exist.
		Procedure procedure = procedureManager.get(Long.parseLong(procedureId));
		System.out.println("Plot ID:" + procedure.getPlotId());
		System.out.println("Watercount:" + procedure.getWatercount());
		System.out.println("Powercount:" + procedure.getPowercount());
		procedure.setWatercount(Double.parseDouble(water));
		procedureManager.save(procedure);
		return "redirect:/editPlot/" + procedure.getPlotId().toString();
	}

	/**
	 * @param model       for the view
	 * @param procedureId is the Id of the procedure
	 * @param power       is the power count
	 * @return a redirect string
	 */
	@PostMapping("/procedureDetails/editPowercount")
	public String savePowercount(Model model, @RequestParam("procedureId") String procedureId,
								 @RequestParam String power) {
		Procedure procedure = procedureManager.get(Long.parseLong(procedureId));
		procedure.setPowercount(Double.parseDouble(power));
		procedureManager.save(procedure);
		return "redirect:/editPlot/" + procedure.getPlotId().toString();
	}

	/**
	 * @param model     for the view
	 * @param procedure as {@link Procedure}
	 * @return a redirect string
	 */
	@PostMapping("/procedureDetails/editPowercount/{plotId}")
	public String savePowercount(Model model, Procedure procedure) {
		// those three lines are just for checking the value. Not necessary to exist.
		System.out.println("Plot ID:" + procedure.getPlotId());
		System.out.println("Powercount:" + procedure.getPowercount());
		procedureManager.save(procedure);
		return "redirect:/procedureDetails";
	}


	/**
	 * Create model with needed information to show a form to change the saved details of the {@link Plot}
	 *
	 * @param user as {@link UserAccount}
	 * @param plot {@link Plot} for which a form with the saves details should be shown
	 * @param mav  {@link ModelAndView} which contains the needed information of the {@link Plot}
	 * @return response as {@link ModelAndView}
	 */
	@GetMapping("/editPlot/{plot}")
	public ModelAndView editPlot(@LoggedIn UserAccount user, @PathVariable Plot plot, ModelAndView mav) {
		System.out.println(procedureManager.getPlotService().findById(plot.getId()) + " PLOT");
		try {
			mav.addObject("plot", procedureManager.getPlotService().findById(plot.getId()));
		} catch (Exception e) {
			mav.addObject("error", e);
			mav.setViewName("error");
			return mav;
		}
		Procedure proc = procedureManager.getActualProcedure(plot);
		//decide if user can change powercount
		Tenant tenant = procedureManager.getTenantManager().getTenantByUserAccount(user);

		boolean tenantOrBoss = false;

		if (proc != null) { // Tenant ?
			if (tenant.getId() == proc.getMainTenant()
					|| proc.getSubTenants().contains(tenant.getId())) {
				tenantOrBoss = true;
			}

			Procedure oldProc = procedureManager.getProcedure(proc.getYear() - 1,
					procedureManager.getPlotService().findById(plot.getId()));

			if (oldProc == null) { // Add old procedure information
				try {
					System.out.println("OLDPROC is null");
					mav.addObject("oldWater", 0);
					mav.addObject("oldPower", 0);
				} catch (Exception e) {
					mav.addObject("error", e);
					mav.setViewName("error");
					return mav;
				}
			} else {
				try {
					mav.addObject("oldWater", "" + oldProc.getWatercount());
					mav.addObject("oldPower", "" + oldProc.getPowercount());
				} catch (Exception e) {
					mav.addObject("error", e);
					mav.setViewName("error");
					return mav;
				}
			}
			try { // add Procedure information
				mav.addObject("procedure", proc);
				mav.addObject("procedureExists", true);
			} catch (Exception e) {
				mav.addObject("error", e);
				mav.setViewName("error");
				return mav;
			}
		} else {
			try {
				System.out.println("PROC is null");
				mav.addObject("procedureExists", false);
			} catch (Exception e) {
				mav.addObject("error", e);
				mav.setViewName("error");
				return mav;
			}
		}

		for (Role role : user.getRoles().toList()) { // Vorstand ?
			System.out.println(role.toString());
			if (role.toString().equalsIgnoreCase("Vorstandsvorsitzender")) {
				tenantOrBoss = true;
			}
		}
		mav.addObject("tenantOrBoss", tenantOrBoss);
		mav.setViewName("plot/editPlot");
		return mav;
	}
}
