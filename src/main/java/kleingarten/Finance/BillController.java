package kleingarten.Finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillController {

	private BillManager billManager;

	@Autowired
	public BillController(BillManager billManager) {
		this.billManager = billManager;
	}

	@GetMapping("/bill")
	String viewBill(Model model){
		model.addAttribute("Bill", billManager.findAll());
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

}
