package kleingarten.complaint;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


// WIP we still have to do this, but if someone got time, they can start working on this
@Controller
public class ComplaintController {

	private ComplaintManager complaintManager;

	ComplaintController(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}

	@PreAuthorize("hasRole('Hauptpächter')")
	@GetMapping("/complaints")
	String complains(Model model) {
		model.addAttribute("complaints", complaintManager.getAll());
		return "complaint/complaints";
	}

	@PreAuthorize("hasRole('Hauptpächter')")
	@GetMapping("/addComplaint/{plot}")
	String addComplaint(Model model) {
		model.addAttribute("complaints", complaintManager.getAll());
		return "complaint/addComplaint";
	}
}
