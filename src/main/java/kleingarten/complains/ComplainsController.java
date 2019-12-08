package kleingarten.complains;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


// WIP we still have to do this, but if someone got time, they can start working on this
@Controller
public class ComplainsController {

	private ComplainsManager complainsManager;

	ComplainsController(ComplainsManager complainsManager){
		this.complainsManager = complainsManager;
	}
	@GetMapping("/complains")
	String complains(Model model) {
		model.addAttribute("complains", complainsManager.getAll());
		return "complains/complains";
	}
}
