package kleingarten.finance;

import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FeeController {

	private ProcedureManager procedureManager;

	@Autowired
	public FeeController(ProcedureManager procedureManager) {
		this.procedureManager = procedureManager;
	}

	@PostMapping(value = "/PDF", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> bill(@RequestParam String procedureId) {

		Procedure mainProcedure = procedureManager.get(Long.parseLong(procedureId));

		Procedure oldProcedure = procedureManager.getProcedure(mainProcedure.getYear() - 1, mainProcedure.getPlotId());

		Bill billToShow = new Bill(mainProcedure, oldProcedure);

		ByteArrayInputStream bis = GeneratePDFBill.bill(billToShow.feeList, mainProcedure.getPlot(), procedureManager.getTenantManager().get(mainProcedure.getMainTenant()), mainProcedure.getYear());

		var headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=Rechnungen.pdf");

		return ResponseEntity
				.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping("bill/{plot}")
	public String viewBill(Model model, @PathVariable Plot plot, @LoggedIn UserAccount userAccount) {

		model.addAttribute("plot", procedureManager.getPlotService().findById(plot.getId()));


		List<Procedure> toDisplay = new ArrayList<Procedure>(), procedures = procedureManager.getAllByPlot(plot).toList();

		Tenant tenant = procedureManager.getTenantManager().getTenantByUserAccount(userAccount);

		if (tenant.hasRole("Vorstandsvorsitzender")) {
			model.addAttribute("procedures", procedures);
		} else {
			for (Procedure proc : procedures) {
				if (proc.isTenant(tenant.getId())) {
					toDisplay.add(proc);
				}
			}
			model.addAttribute("procedures", toDisplay);
		}

		return "finance/bill";
	}

	public String finalizeProcedures(Model model, @PathVariable String year) {

		List<Procedure> procedures = procedureManager.getAllByYear(Integer.parseInt(year)).toList();
		for (Procedure proc : procedures) {
			System.out.println(proc.toString());
		}

		return "nix";
	}
}
