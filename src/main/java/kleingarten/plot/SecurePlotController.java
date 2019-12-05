package kleingarten.plot;

import kleingarten.Finance.Procedure;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
				Procedure procedure = dataService.getProcedure(LocalDateTime.now().getYear(), plot);
				if (procedure.isTenant(tenant.getId())) {
					return rentedPlots(user);
				}

				plotControllerService.secureSetAccessRightForPlotDetails(plot, tenant, mav);
				plotControllerService.secureGetInformationOfProcedure(procedure, mav);
			} else {
				mav.addObject("canApply", true);
			}
		} catch (Exception e) {
			mav.addObject("error", e);
			mav.setViewName("error");
			return mav;
		}
		mav.setViewName("plot/myPlot");

		return mav;
	}

	/**
	 * Create model with information of a {@link Plot} which is rented by the user to show the detail page of the
	 * {@link Plot} when a user is logged in
	 * @param user {@link UserAccount} of the logged in user
	 * @return response as {@link ModelAndView}
	 */
	@GetMapping("/myPlot")
	public ModelAndView rentedPlots(@LoggedIn UserAccount user) {
		ModelAndView mav = new ModelAndView();
		Plot shownPlot;

		try {
			Tenant tenant = tenantManager.getTenantByUserAccount(user);

			Set<Plot> plots = dataService.getRentedPlots(LocalDateTime.now().getYear(), tenant);
			shownPlot = plots.iterator().next();

			Map<Plot, String> rentedPlots = new HashMap<>();
			for (Plot parcel : plots) {
				rentedPlots.put(parcel, parcel.getName());
			}

			plotControllerService.addGeneralInformationOfPlot(shownPlot, mav);
			mav.addObject("canSee", true);
			mav.addObject("canSeeBills", true);
			mav.addObject("canSeeApplications", true);
			mav.addObject("canModify", true);
			mav.addObject("plots", rentedPlots);
		} catch (Exception e) {
			mav.addObject("error", e);
			mav.setViewName("error");
			return mav;
		}
		mav.setViewName("plot");

		return mav;
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
			plotControllerService.secureSetPlotColor(plot, user, colors);
			plotControllerService.secureSetAccessRightForPlot(plot, user, rights);
		}

		mav.addObject("plotList", plots);
		mav.addObject("plotColors", colors);
		mav.addObject("userRights", rights);
		mav.setViewName("plot/plotOverview");

		return mav;

	}
}
