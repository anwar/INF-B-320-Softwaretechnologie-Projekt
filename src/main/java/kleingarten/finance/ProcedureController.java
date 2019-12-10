package kleingarten.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import kleingarten.plot.Plot;

@Controller
public class ProcedureController {
	private ProcedureManager procedureManager;

	@Autowired

	public ProcedureController(ProcedureManager procedureManager) {
		this.procedureManager = procedureManager;
	}

	/**
	 * Provide simple Lists of procedure
	 * @param model
	 * @return
	 */
	@GetMapping("/procedure")
	//@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String procedure(Model model) {
		model.addAttribute("procedureList", procedureManager.getAll());
		return "finance/procedure";
	}

	/**
	 *By clicking: able to see all the details of selected procedure.
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/procedureDetails")
	//@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	String procedureDetails(@RequestParam("id") String id, Model model) {
		model.addAttribute("procedure", procedureManager.get(Long.parseLong(id)));
		return "finance/procedureDetails";
	}

	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/updateProcedure")
	String updateProcedure(Model model) {
		model.addAttribute("procedure", procedureManager.getAll());
		return "finance/updateProcedure";
	}

	/**
	 * Obmann will be able to change the value of watercount
	 * @param model
	 * @param plotId
	 * @return
	 */
	@GetMapping("/procedureDetails/editWatercount/{plotId}")
	//@PreAuthorize("hasRole('ROLE_VORSTAND')")
	public String editWatercount(Model model, @PathVariable String plotId) {
		model.addAttribute("procedure", procedureManager.findByPlotId(plotId).get());
		return "finance/editWatercount";
	}

	/**
	 * @param model
	 * @param procedure
	 * @return
	 */
	@PostMapping("/procedureDetails/editWatercount")
	public String saveWatercount(Model model, @RequestParam("procedureId") String procedureId, @RequestParam String water) {
		// those three lines are just for checking the value. Not necessary to exist.
		Procedure procedure = procedureManager.get(Long.parseLong(procedureId));
		System.out.println("Plot ID:" + procedure.getPlotId());
		System.out.println("Watercount:" + procedure.getWatercount());
		System.out.println("Powercount:" + procedure.getPowercount());
		procedure.setWatercount(Double.parseDouble(water));
		procedureManager.save(procedure);
		return "redirect:/editPlot/"+procedure.getPlotId().toString();
	}

	/**
	 * @param model
	 * @param plotId
	 * @return
	 */
	@GetMapping("procedureDetails/editPowercount/{plotId}")
	//@PreAuthorize("hasRole('ROLE_VORSTAND')")
	public String editCount(Model model, @PathVariable String plotId) {
		model.addAttribute("procedure", procedureManager.findByPlotId(plotId).get());
		return "finance/editPowercount";
	}

	/**
	 * @param model
	 * @param procedure
	 * @return
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
	 * @param plot {@link Plot} for which a form with the saves details should be shown
	 * @param mav  {@link ModelAndView} which contains the needed information of the {@link Plot}
	 * @return response as {@link ModelAndView}
	 */
	@GetMapping("/editPlot/{plot}")
	public ModelAndView editPlot(@PathVariable Plot plot, ModelAndView mav) {
		
		System.out.println(procedureManager.getPlotService().findById(plot.getId())+" PLOT");
		
		try {
			mav.addObject("plot", procedureManager.getPlotService().findById(plot.getId()));
		} catch (Exception e) {
			mav.addObject("error", e);
			mav.setViewName("error");
			return mav;
		}
		Procedure proc = (plot==null) ? null : procedureManager.getActualProcedure(plot);
		Procedure oldProc = (proc==null) ? null : procedureManager.getProcedure(proc.getYear()-1, procedureManager.getPlotService().findById(plot.getId()));
		if(oldProc==null) { // Add old procedure information
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
				mav.addObject("oldWater", ""+oldProc.getWatercount());
				mav.addObject("oldPower", ""+oldProc.getPowercount());
			} catch (Exception e) {
				mav.addObject("error", e);
				mav.setViewName("error");
				return mav;
			}
			
		}
		if(proc == null) {
			try {
				System.out.println("PROC is null");
				mav.addObject("procedureExists", false);
			} catch (Exception e) {
				mav.addObject("error", e);
				mav.setViewName("error");
				return mav;
			}
		} else {
			try { // add Procedure information
				mav.addObject("procedure", proc);
				mav.addObject("procedureExists", true);
			} catch (Exception e) {
				mav.addObject("error", e);
				mav.setViewName("error");
				return mav;
			}
		}
		mav.setViewName("plot/editPlot");
		return mav;
	}

}
