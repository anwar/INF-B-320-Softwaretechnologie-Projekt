package kleingarten.finance;

import kleingarten.plot.Plot;

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
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
public class FeeController {

	private ProcedureManager procedureManager;

	@Autowired
	public FeeController(ProcedureManager procedureManager) {
		this.procedureManager = procedureManager;
	}

	@GetMapping(value = "/PDF", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> bill(Procedure mainProcedure, Procedure oldProcedure) { //oldProcedure can be null
		//I dicide to pass plotid and year instead, we could make two functions: currentBillAndFinalizeProcedure(plotId) and bill(plotId, year)
		//both can get the main and old Procedure from procedureManager, so you can simply work with those procedures in the body right now.

		Bill billToShow = new Bill(mainProcedure, oldProcedure);

		ByteArrayInputStream bis = GeneratePDFBill.bill( billToShow.feeList ); //you may add a getter for feelist

		var headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=Rechnungen.pdf");

		return ResponseEntity
				.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@PreAuthorize("hasRole('Hauptpächter') || hasRole('Nebenpächter')")
	@GetMapping("bill/{plot}")
	public String viewBill(Model model, @PathVariable Plot plot, @LoggedIn UserAccount userAccount){

		model.addAttribute("plot", procedureManager.getPlotService().findById(plot.getId()));

		Procedure mainProcedure = procedureManager.getActualProcedure(plot); // need to initialize mainProcedure	//getCurrentBillAndFinalizeProcedure(plot);
		mainProcedure = procedureManager.getProcedure(mainProcedure.getYear(), procedureManager.getPlotService().findById(plot.getId())); // getProcedure of mainProcedure
		Procedure oldProcedure = procedureManager.getProcedure(mainProcedure.getYear()-1, procedureManager.getPlotService().findById(plot.getId())); // getProcedure of oldProcedure
		//new Bill(mainProcedure, oldProcedure);

		return "finance/bill";
	}
}
