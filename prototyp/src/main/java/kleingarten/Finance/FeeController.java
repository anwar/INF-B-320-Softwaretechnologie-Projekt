package kleingarten.Finance;

import org.salespointframework.core.SalespointIdentifier;

//import kleingarten.Finance.Fee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FeeController {
	private FeeManagement feeManagement;

	@Autowired
	public FeeController(FeeManagement feeManagement) {

		this.feeManagement = feeManagement;
	}

	protected FeeController(){}

	@GetMapping("/bill")
	public String viewFeeCatalog(Model model) {
		model.addAttribute("feeCatalog", feeManagement.findAll());
		return "bill";
	}

	@ModelAttribute("bill")
	public Iterable<Fee> allFee() {
		return feeManagement.findAll();
	}

	@GetMapping("/bill/{billID}")
	public String viewFeeCatalog(Model model, @PathVariable String billID) {
		model.addAttribute("billID", billID);
		model.addAttribute("feeCatalog", feeManagement.getByBillId(billID));
		return "bill";
	}

	@PostMapping("/bill/add") // create a new fee
	public String addFee(FeeForm form) {
		feeManagement.create(form);
		return "redirect:/bill";
	}

	@GetMapping("/bill/add")
	public String feeForm(Model model, FeeForm form) {
		model.addAttribute("feeForm", form);
		return "addFee";
	}

	@GetMapping("/bill/{Id}/delete")
	public String deleteFee(@PathVariable(value = "Id") SalespointIdentifier Id) {
		feeManagement.delete(Id);
		return "redirect:/bill";
	}

	// editing fee button is also implemented and the "edit Fee" button works, but when I edit values and click "submit" button, it goes to the white label pages.
	@GetMapping("/bill/{Id}/edit")
	public String editFee(Model model, @PathVariable SalespointIdentifier Id) {
		model.addAttribute("fee", feeManagement.findById(Id).get());
		return "editFee";		// need to work on editFee html.
	}
	@PostMapping("/bill/{Id}/edit")
	public String saveFee(Model model, Fee fee) {
		System.out.println("ID:" + fee.getId());
		System.out.println("Name:" + fee.getName());
		feeManagement.save(fee);
		return "redirect:/bill";
	}
}


