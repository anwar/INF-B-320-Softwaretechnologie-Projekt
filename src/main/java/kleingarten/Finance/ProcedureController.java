package kleingarten.Finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProcedureController {
	private ProcedureManager procedureManager;

	@Autowired

	public ProcedureController(ProcedureManager procedureManager ) {
		this.procedureManager = procedureManager;


	}

	@GetMapping("/bill")
	String viewBill(){
		return "bill";
	}
/*
	@GetMapping("/bill")
	public String viewBill(Model model) {
		model.addAttribute("ProcedureRepository", procedureManager.findAll());
		return "/bill";
	}

	@ModelAttribute("/bill")
	public Iterable<Procedure> allProcedure() {
		return procedureManager.findAll();
	}
 */

	/**
	 * @param model
	 * @param plotId
	 * @return
	 */
	// there is an issue to call plotId. [sanghyun]
	@GetMapping("procedure/editWatercount/{plotId}")
	//@PreAuthorize("hasRole('ROLE_VORSTAND')")
	public String editWatercount(Model model, @PathVariable String plotId) {
		model.addAttribute("procedure", procedureManager.findByPlotId(plotId).get());
		return "editWatercount";
	}

	/**
	 * @param model
	 * @param procedure
	 * @return
	 */
	@PostMapping("procedure/editWatercount/{plotId}")
	public String saveWatercount(Model model, Procedure procedure) {
		// those three lines are just for checking the value. Not necessary to exist.
		System.out.println("Plot ID:" + procedure.getPlotId());
		System.out.println("Watercount:" + procedure.getWatercount());
		System.out.println("Powercount:" + procedure.getPowercount());
		procedureManager.save(procedure);
		return "redirect:/procedure";
	}

	// there is an issue to call plotId. [sanghyun]
	@GetMapping("procedure/editPowercount/{plotId}")
	//@PreAuthorize("hasRole('ROLE_VORSTAND')")
	public String editCount(Model model, @PathVariable String plotId) {
		model.addAttribute("procedure", procedureManager.findByPlotId(plotId).get());
		return "editPowercount";
	}
	/**
	 * @param model
	 * @param procedure
	 * @return
	 */
	@PostMapping("procedure/editPowercount/{plotId}")
	public String savePowercount(Model model, Procedure procedure) {
		// those three lines are just for checking the value. Not necessary to exist.
		System.out.println("Plot ID:" + procedure.getPlotId());
		System.out.println("Powercount:" + procedure.getPowercount());
		procedureManager.save(procedure);
		return "redirect:/procedure";
	}

	@GetMapping("procedure")
	String procedure(Model model){
		model.addAttribute("procedure", procedureManager.getAll());
		return "procedure";
	}


}
