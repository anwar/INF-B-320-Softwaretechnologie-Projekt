package kleingarten.finance;

import kleingarten.message.MessageService;
import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
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

import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FeeController {

	private ProcedureManager procedureManager;
	private MessageService messageService;
	private TenantManager tenantManager;

	public FeeController(@Autowired ProcedureManager procedureManager,
						 @Autowired MessageService messageService,
						 @Autowired TenantManager tenantManager) {
		this.procedureManager = procedureManager;
		this.messageService = messageService;
		this.tenantManager = tenantManager;
	}

	@PostMapping(value = "/PDF", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> bill(@RequestParam String procedureId) {

		Procedure mainProcedure = procedureManager.get(Long.parseLong(procedureId));

		ByteArrayInputStream bis = getBillPDFBytes(mainProcedure);

		var headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=Rechnungen.pdf");

		return ResponseEntity
				.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	/**
	 * Method to send Bill by Email.
	 *
	 * @param procedureId as {@link String}
	 * @param plotId      as {@link String} needed to make a redirect to the bills of a plot
	 * @return view as {@link String}
	 */
	@PostMapping(value = "/send-by-email")
	public String sendBillByEmail(@RequestParam String procedureId,
								  @RequestParam String plotId) {

		Procedure mainProcedure = procedureManager.get(Long.parseLong(procedureId));
		int currentYear = mainProcedure.getYear();
		String tenantEmail = tenantManager.get(mainProcedure.getMainTenant()).getEmail();

		byte[] pdfBytes = getBillPDFBytes(mainProcedure).readAllBytes();
		ByteArrayDataSource attachmentDataSource = new ByteArrayDataSource(
				pdfBytes, MediaType.APPLICATION_PDF.toString());


		messageService.sendMessageWithAttachment(tenantEmail,
				"Rechung fuer " + currentYear,
				"Im Anhang dieser Email finden Sie Ihre Rechnung fuer " + currentYear,
				"Rechnungen-" + currentYear + ".pdf",
				attachmentDataSource);

		return "redirect:/bill/" + plotId;
	}

	/**
	 * Method to show Bill overview page.
	 *
	 * @param plot        as {@link Plot}
	 * @param model       as {@link Model} to add needed information
	 * @param userAccount as {@link UserAccount}
	 * @return view as {@link String}
	 */
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
	 *
	 * @param plot  {@link Plot} needed to make a redirect to the bills of a plot
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
			procedureManager.save(newProcedure);
		}
		model.addAttribute("plot", plot);

		return "redirect:/bill/" + plot.getId();
	}

	ByteArrayInputStream getBillPDFBytes(Procedure currentYearProcedure) {
		Procedure oldProcedure = procedureManager.getProcedure(currentYearProcedure.getYear() - 1, currentYearProcedure.getPlotId());
		Bill billToShow = new Bill(currentYearProcedure, oldProcedure);

		return GeneratePDFBill.bill(
				billToShow.feeList, currentYearProcedure.getPlot(),
				procedureManager.getTenantManager().get(currentYearProcedure.getMainTenant()), currentYearProcedure.getYear());
	}
}
