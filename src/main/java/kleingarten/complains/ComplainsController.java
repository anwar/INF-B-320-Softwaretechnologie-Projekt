package kleingarten.complains;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


// WIP we still have to do this, but if someone got time, they can start working on this
@Controller
public class ComplainsController {

	private ComplainsManager complainsManager;

	ComplainsController(ComplainsManager complainsManager){
		this.complainsManager = complainsManager;
	}

	@PreAuthorize("hasRole('Hauptpächter')")
	@GetMapping("/complains")
	String complains(Model model) {
		model.addAttribute("complains", complainsManager.getAll());
		return "complains/complains";
	}

	@PreAuthorize("hasRole('Hauptpächter')")
	@GetMapping("/addComplains/{plot}")
	String addComplains(Model model){
		model.addAttribute("complain", complainsManager.getAll());
		return "complains/addComplains";
	}
}
