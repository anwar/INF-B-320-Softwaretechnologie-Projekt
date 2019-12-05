package kleingarten.plot;

import kleingarten.Finance.Procedure;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.money.format.MonetaryFormats;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class SecurePlotController {
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;
	private final TenantRepository tenantRepository;
	private final DataService dataService;
	private final PlotControllerService plotControllerService;

	SecurePlotController(PlotService plotService, PlotCatalog plotCatalog, TenantRepository tenantRepository,
						   DataService dataService, PlotControllerService plotControllerService) {
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.tenantRepository = tenantRepository;
		this.dataService = dataService;
		this.plotControllerService = plotControllerService;
	}

	public ModelAndView detailsOfPlot(@LoggedIn UserAccount user, @RequestParam Optional<Plot> plot) {
		ModelAndView mav = new ModelAndView();
		Plot shownPlot = null;

		if (plot.isPresent()) {
			if (!plotService.existsByName(plot.get().getName())) {
				throw new IllegalArgumentException("Plot must exist!");
			}
			shownPlot = plot.get();
		}

			if (tenantRepository.findByUserAccount(user).isEmpty()) {
				throw new IllegalArgumentException("User must exist!");
			}
			Tenant tenant = tenantRepository.findByUserAccount(user).get();

			if (plot.isEmpty()) {
				Set<Plot> plots = dataService.getRentedPlots(LocalDateTime.now().getYear(), tenant);
				shownPlot = plots.iterator().next();

				Map<Plot, String> rentedPlots = new HashMap<>();
				for (Plot parcel : plots) {
					rentedPlots.put(parcel, parcel.getName());
				}

				mav.addObject("canSee", true);
				mav.addObject("canSeeBills", true);
				mav.addObject("canSeeApplications", true);
				mav.addObject("canModify", true);
				mav.addObject("plots", rentedPlots);
			}

			if (dataService.procedureExists(LocalDateTime.now().getYear(), shownPlot)) {
				mav.addObject("canSee", true);

				Procedure procedure = dataService.getProcedure(LocalDateTime.now().getYear(), shownPlot);
				mav.addObject("mainTenant", dataService.findTenantById(procedure.getMainTenant()));

				Set<Tenant> subTenants = new HashSet<>();
				for (long tenantId : procedure.getSubTenants()) {
					subTenants.add(dataService.findTenantById(tenantId));
				}

				mav.addObject("subTenants", subTenants);
				mav.addObject("workHours", procedure.getWorkMinutes() + "min");
			} else {
				mav.addObject("canApply", true);
			}

			// Check which role the user has to provide the correct rights of access on the view
			if (dataService.tenantHasRole(tenant, Role.of("Kassierer"))) {
				mav.addObject("canSeeBills", true);
				boolean condModify = dataService.tenantHasRole(tenant, Role.of("Obmann"))
						|| dataService.tenantHasRole(tenant, Role.of("Wassermann"));
				mav.addObject("canModify", condModify);
			} else if (dataService.tenantHasRole(tenant, Role.of("Obmann"))
					|| dataService.tenantHasRole(tenant, Role.of("Wassermann"))) {
				mav.addObject("canModify", true);
			} else if (dataService.tenantHasRole(tenant, Role.of("Vorstandsvorsitzender"))
					|| dataService.tenantHasRole(tenant, Role.of("Stellvertreter"))) {
				mav.addObject("canSeeBills", true);
				mav.addObject("canSeeApplications", true);
				mav.addObject("canModify", true);
			}

		// Add default information to the model
		mav.addObject("plotID", shownPlot.getId());
		mav.addObject("plotName", shownPlot.getName());
		mav.addObject("plotSize", shownPlot.getSize() + " m²");
		mav.addObject("plotDescription", shownPlot.getDescription());
		mav.addObject("plotPrice", MonetaryFormats.getAmountFormat(Locale.GERMANY)
				.format(shownPlot.getEstimator()));

		mav.setViewName("plot/myPlot");

		return mav;
	}

	@GetMapping("/myPlot")
	public ModelAndView rentedPlots(@LoggedIn UserAccount user) {
		return detailsOfPlot(user, Optional.empty());
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
