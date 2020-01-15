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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
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

	/**
	 * Method to close all {@link Procedure}s of the last year and create new ones
	 * @param plot {@link Plot} needed to make a redirect to the bills of a plot
	 * @param model {@link Model} to add needed information
	 * @return view as {@link String}
	 */
	@PreAuthorize("hasAnyRole('Vorstandsvorsitzender', 'Stellvertreter')")
	@GetMapping("/finalize/{plot}")
	public String finalizeProcedures(@PathVariable Plot plot, Model model) {
		int year = LocalDateTime.now().getYear();
		List<Procedure> procedures = procedureManager.getAllByYear(year - 1).toList();
		for (Procedure proc : procedures) {
			proc.close();
			Procedure newProcedure = new Procedure(year, proc.getPlot(), proc.getMainTenant());
			newProcedure.setSize(proc.getSize());
			newProcedure.setPowercount(proc.getPowercount());
			newProcedure.setWatercount(proc.getWatercount());
			if (proc.getWorkMinutes() > 240) {
				newProcedure.setWorkMinutes(proc.getWorkMinutes() - 240);
			}
			if (procedureManager.getProcedure(year, plot) == null) {
				procedureManager.save(newProcedure);
			}
		}
		model.addAttribute("plot", plot);

		return "redirect:/bill/" + plot.getId();
	}
}
