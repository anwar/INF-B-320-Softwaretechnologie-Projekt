package kleingarten.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
	@PostMapping("/procedureDetails/editWatercount/{plotId}")
	public String saveWatercount(Model model, Procedure procedure) {
		// those three lines are just for checking the value. Not necessary to exist.
		System.out.println("Plot ID:" + procedure.getPlotId());
		System.out.println("Watercount:" + procedure.getWatercount());
		System.out.println("Powercount:" + procedure.getPowercount());
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

}
