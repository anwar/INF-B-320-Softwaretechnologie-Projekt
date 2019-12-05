package kleingarten.plot;

import kleingarten.tenant.TenantRepository;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.money.format.MonetaryFormats;
import java.util.*;

@Controller
public class InsecurePlotController {
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;
	private final TenantRepository tenantRepository;
	private final DataService dataService;
	private final PlotControllerService plotControllerService;
	private final SecurePlotController securePlotController;

	InsecurePlotController(PlotService plotService, PlotCatalog plotCatalog, TenantRepository tenantRepository,
						   DataService dataService, PlotControllerService plotControllerService,
						   SecurePlotController securePlotController) {
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.tenantRepository = tenantRepository;
		this.dataService = dataService;
		this.plotControllerService = plotControllerService;
		this.securePlotController = securePlotController;
	}

	/**
	 * Use different methods to show the overview of all {@link Plot}s depending if a user is logged in or not
	 * @return response as {@link ModelAndView}
	 */
	@GetMapping("/anlage")
	public ModelAndView plotOverview(@LoggedIn Optional<UserAccount> user) {
		if (user.isEmpty()) {
			return insecurePlotOverview();
		}
		return securePlotController.plotOverview(user.get());
	}

	@GetMapping("/plot/{plot}")
	public ModelAndView detailsOfPlot(@LoggedIn Optional<UserAccount> user, @PathVariable Plot plot) {
		if (user.isEmpty()) {
			return detailsOfFreePlot(plot);
		}
		return securePlotController.detailsOfPlot(user.get(), Optional.of(plot));
	}

	public ModelAndView detailsOfFreePlot(@PathVariable Plot plot) {
		ModelAndView mav = new ModelAndView();
		Plot shownPlot = null;

			if (!plotService.existsByName(plot.getName())) {
				throw new IllegalArgumentException("Plot must exist!");
			}
			shownPlot = plot;

			if (plot.getStatus() == PlotStatus.TAKEN) {
				throw new IllegalArgumentException("Unauthenticated user must not have access to a rented plot!");
			}
			mav.addObject("canApply", true);


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

	/**
	 * Create model with needed information to show the overview of all {@link Plot}s when no user is logged in
	 * @return response as {@link ModelAndView}
	 */
	public ModelAndView insecurePlotOverview() {
		ModelAndView mav = new ModelAndView();

		Set<Plot> plots = plotCatalog.findAll().toSet();
		Map<Plot, String> colors = new HashMap<>();
		Map<Plot, Boolean> rights = new HashMap<>();

		for (Plot plot : plots) {
			plotControllerService.insecureSetPlotColor(plot, colors);
			plotControllerService.insecureSetAccessRightForPlot(plot, rights);
		}

		mav.addObject("plotList", plots);
		mav.addObject("plotColors", colors);
		mav.addObject("userRights", rights);
		mav.setViewName("plot/plotOverview");

		return mav;

	}
}
