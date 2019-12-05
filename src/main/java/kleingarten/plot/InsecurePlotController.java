package kleingarten.plot;

import kleingarten.tenant.TenantRepository;
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

	InsecurePlotController(PlotService plotService, PlotCatalog plotCatalog, TenantRepository tenantRepository,
						   DataService dataService, PlotControllerService plotControllerService) {
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.tenantRepository = tenantRepository;
		this.dataService = dataService;
		this.plotControllerService = plotControllerService;
	}

	@GetMapping("/plot/{plot}")
	public ModelAndView detailsOfPlot(@PathVariable Optional<Plot> plot) {
		ModelAndView mav = new ModelAndView();
		Plot shownPlot = null;

		if (plot.isPresent()) {
			if (!plotService.existsByName(plot.get().getName())) {
				throw new IllegalArgumentException("Plot must exist!");
			}
			shownPlot = plot.get();
		}

			if (plot.get().getStatus() == PlotStatus.TAKEN) {
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
	@GetMapping("/anlage")
	public ModelAndView plotOverview() {
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
