package kleingarten.plot;

import kleingarten.Finance.Procedure;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class SecurePlotController {
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;
	private final TenantManager tenantManager;
	private final DataService dataService;
	private final PlotControllerService plotControllerService;

	SecurePlotController(PlotService plotService, PlotCatalog plotCatalog, TenantManager tenantManager,
						   DataService dataService, PlotControllerService plotControllerService) {
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.tenantManager = tenantManager;
		this.dataService = dataService;
		this.plotControllerService = plotControllerService;
	}

	/**
	 * Create model with information of a {@link Plot} to show the detail page of a {@link Plot} when a user
	 * is logged in
	 * @param user {@link UserAccount} of the logged in user
	 * @param plot {@link Plot} of which information should be shown
	 * @return response as {@link ModelAndView}
	 */
	public ModelAndView detailsOfPlot(@LoggedIn UserAccount user, Plot plot) {
		ModelAndView mav = new ModelAndView();

		if (!plotService.existsByName(plot.getName())) {
			mav.addObject("error","Plot must exist!");
			mav.setViewName("error");
			return mav;
		}

		plotControllerService.addGeneralInformationOfPlot(plot, mav);
		try {
			Tenant tenant = tenantManager.getTenantByUserAccount(user);
			if (dataService.procedureExists(LocalDateTime.now().getYear(), plot)) {
				mav.addObject("rented", true);
				Procedure procedure = dataService.getProcedure(LocalDateTime.now().getYear(), plot);

				plotControllerService.secureSetAccessRightForPlotDetails(Optional.of(procedure), tenant, mav);
				plotControllerService.addProcedureInformationOfPlot(procedure, tenant, mav);
				if (procedure.isTenant(tenant.getId())) {
					return detailsOfRentedPlot(user, plot, mav);
				}
			}
			if (plot.getStatus() == PlotStatus.FREE) {
				plotControllerService.secureSetAccessRightForPlotDetails(Optional.empty(), tenant, mav);
				mav.addObject("rented", false);
			}
			mav.addObject("subTenants", new HashMap<Tenant, String>());
			mav.addObject("plots", new HashMap<Plot, String>());
		} catch (Exception e) {
			mav.addObject("error", e);
			mav.setViewName("error");
			return mav;
		}
		System.out.println(plot.getId().toString());
		mav.setViewName("plot/myPlot");
		return mav;
	}

	/**
	 * Create model with information of a {@link Plot} which is rented by the user to show the detail page of the
	 * {@link Plot} when a user is logged in and accesses his rented plots by using the entry in the navigation bar
	 * @param user {@link UserAccount} of the logged in user
	 * @return response as {@link ModelAndView}
	 */
	@GetMapping("/myPlot/")
	public ModelAndView rentedPlots(@LoggedIn UserAccount user) {
		ModelAndView mav = new ModelAndView();
		Plot shownPlot;

		try {
			Tenant tenant = tenantManager.getTenantByUserAccount(user);

			Set<Plot> plots = dataService.getRentedPlots(LocalDateTime.now().getYear(), tenant);
			shownPlot = plots.iterator().next();

			Procedure procedureOfShownPlot = dataService.getProcedure(LocalDateTime.now().getYear(), shownPlot);
			plotControllerService.addGeneralInformationOfPlot(shownPlot, mav);
			plotControllerService.secureSetAccessRightForPlotDetails(Optional.of(procedureOfShownPlot), tenant, mav);
			plotControllerService.addProcedureInformationOfPlot(procedureOfShownPlot, tenant, mav);
			mav.addObject("rents", true);
			if (procedureOfShownPlot.getMainTenant() == tenant.getId()) {
				mav.addObject("isMainTenant", true);
			}
		} catch (Exception e) {
			mav.addObject("error", e);
			mav.setViewName("error");
			return mav;
		}
		return detailsOfRentedPlot(user, shownPlot, mav);
	}

	/**
	 * Create model with information of a {@link Plot} which is rented by the user to show the detail page of the
	 * {@link Plot} with buttons for all rented {@link Plot}s of this user when a user is logged in
	 * @param user {@link UserAccount} of the logged in user
	 * @param plot {@link Plot} of which information should be shown
	 * @param modelAndView {@link ModelAndView} with the information of the {@link Plot}
	 * @return response as {@link ModelAndView}
	 */
	@GetMapping("/myPlot/{plot}")
	public ModelAndView detailsOfRentedPlot(@LoggedIn UserAccount user, @PathVariable Plot plot,
											ModelAndView modelAndView) {
		Map<Plot, String> rentedPlots = new HashMap<>();
		plotControllerService.secureGetRentedPlotsForTenant(tenantManager.getTenantByUserAccount(user), rentedPlots);
		modelAndView.addObject("plots", rentedPlots);

		modelAndView.setViewName("/plot/myPlot");
		return modelAndView;
	}

	/**
	 * Create model with needed information to show the overview of all {@link Plot}s when user is logged in
	 * @param user {@link UserAccount} of the logged in user
	 * @return response as {@link ModelAndView}
	 */
	public ModelAndView plotOverview(@LoggedIn UserAccount user) {
		ModelAndView mav = new ModelAndView();

		Set<Plot> plots = plotCatalog.findAll().toSet();
		Map<Plot, String> colors = new HashMap<>();
		Map<Plot, Boolean> rights = new HashMap<>();

		for (Plot plot : plots) {
			plotControllerService.secureSetPlotColor(plot, colors);
			plotControllerService.secureSetAccessRightForPlot(plot, user, rights);
		}

		mav.addObject("plotList", plots);
		mav.addObject("plotColors", colors);
		mav.addObject("userRights", rights);
		mav.setViewName("plot/plotOverview");

		return mav;

	}

	@GetMapping("/chairmenOverview")
	public ModelAndView chairmenOverview(@LoggedIn UserAccount user) {
		ModelAndView mav = new ModelAndView();
		Set<Plot> plots = plotCatalog.findAll().toSet();
		Map<Plot, Boolean> rights = new HashMap<>();

		Map<Plot, String> administratedPlots = plotControllerService.secureSetColorOfChairmenForPlots();
		for (Plot plot:
			 administratedPlots.keySet()) {
			plotControllerService.secureSetAccessRightForPlot(plot, user, rights);
		}

		mav.addObject("plotList", plots);
		mav.addObject("plotColors", administratedPlots);
		mav.addObject("userRights", rights);
		mav.setViewName("plot/plotOverview");

		return mav;
	}

	@GetMapping("/editPlot/{plot}")
	String editPlot(@PathVariable Plot plot, Model model){
		model.addAttribute("plot", plotService.findById(plot.getId()));
		System.out.println(plot.getId());
		return "plot/editPlot";
	}

	@PostMapping("/editedPlot")
	String editedPlot(@RequestParam(name = "plotID") ProductIdentifier plotId, @RequestParam("size") int size, @RequestParam("description") String description
					 /* @RequestParam() int estimator */){
		plotService.findById(plotId).setSize(size);
		System.out.println(plotId);
		plotService.findById(plotId).setDescription(description);
		//plotService.findById(plotId).setEstimator(Money.of);

		plotCatalog.save(plotService.findById(plotId));
		return "redirect:plot/" + plotId.toString();
	}
}
