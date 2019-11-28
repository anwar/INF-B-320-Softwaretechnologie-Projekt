package kleingarten.Finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ProcedureController {
	//private ProcedureManager procedureManager;

	@Autowired
	public ProcedureController( ) {

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


}
