package kleingarten.Finance;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;

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

//Editing Watercount, but need to solve a problem with plotId.

	@GetMapping("/procedure/editWatercount/{plotId}")
	//@PreAuthorize("hasRole('ROLE_VORSTAND')")
	public String editFee(Model model, @PathVariable String plotId) {
		model.addAttribute("procedure", procedureManager.findByPlotId(plotId).get());
		return "/editWatercount";
	}
	@PostMapping("/procedure/editWatercount/{plotId}")
	public String saveProcedure(Model model, Procedure procedure) {
		System.out.println("Plot ID:" + procedure.getPlotId());
		System.out.println("Watercount:" + procedure.getWatercount());
		procedureManager.save(procedure);
		return "redirect:/procedure";
	}



	@GetMapping("/procedure")
	String procedure(Model model){
		model.addAttribute("procedure", procedureManager.getAll());
		return "procedure";
	}


}
