package kleingarten.Finance;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
public class FeeController {
	private FeeManager feeManager;
	private FeeRepository feeRepository;
	private Procedure procedure;

	@Autowired
	private FeeServiceI feeServiceI;

	@Autowired
	public FeeController(FeeManager feeManager, FeeRepository feeRepository) {
		this.feeManager = feeManager;
		this.feeRepository = feeRepository;
	}

	@GetMapping("fees")
	public String feeList(Model model) {
		model.addAttribute("feeList", feeManager.findAll());
		return "finance/fees";
	}

	@PostMapping("fees/add")
	public String addFee(FeeForm form) {
		feeManager.create(form);
		return "redirect:/fees";
	}

	@GetMapping("fees/add")
	public String feeForm(Model model, FeeForm form) {
		model.addAttribute("feeForm", form);
		return "finance/addFee";
	}

	@GetMapping("fees/{id}/delete")
	public String deleteFee(@PathVariable(value = "id") Long id) {
		feeManager.delete(id);
		return "redirect:/fees";
	}

	@GetMapping("fees/{id}/edit")
	public String editFee(Model model, @PathVariable long id) {
		model.addAttribute("fee", feeManager.findById(id).get());
		return "finance/editFee";
	}

	@PostMapping("fees/{id}/edit")
	public String saveFee(Model model, Fee fee) {
		feeManager.save(fee);
		return "redirect:/fees";
	}

	@GetMapping(value = "/PDF", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> bill() {

		var fees = (List<Fee>) feeServiceI.findAll();

		ByteArrayInputStream bis = GeneratePDFBill.bill(fees);

		var headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=Rechnungen.pdf");

		return ResponseEntity
				.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping("bill")
	public String viewBill(){
		return "finance/bill";
	}
}
