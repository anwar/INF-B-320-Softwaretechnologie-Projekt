package kleingarten.plot;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Controller class which handles all requests taken by an user who is not logged in
 */
@Controller
public class InsecurePlotController {
	private final PlotCatalog plotCatalog;
	private final PlotControllerService plotControllerService;
	private final SecurePlotController securePlotController;

	InsecurePlotController(PlotCatalog plotCatalog,
						   PlotControllerService plotControllerService,
						   SecurePlotController securePlotController) {
		this.plotCatalog = plotCatalog;
		this.plotControllerService = plotControllerService;
		this.securePlotController = securePlotController;
	}

	/**
	 * Use different methods to show the overview of all {@link Plot}s depending if a user is logged in or not
	 *
	 * @param user {@link Optional} of {@link UserAccount}, contains the user's {@link UserAccount} when he is logged in
	 * @return response as {@link ModelAndView}
	 */
	@GetMapping("/anlage")
	public ModelAndView plotOverview(@LoggedIn Optional<UserAccount> user) {
		if (user.isEmpty()) {
			return insecurePlotOverview();
		}
		return securePlotController.plotOverview(user.get());
	}

	/**
	 * Use different methods to show the detail page of a {@link Plot} depending if a user is logged in or not
	 *
	 * @param user  {@link Optional} of {@link UserAccount}, contains the user's {@link UserAccount} when he is logged in
	 * @param plot  {@link Plot} to show the details of
	 * @param model as {@link Model}
	 * @return response as {@link ModelAndView}
	 */
	@GetMapping("/plot/{plot}")
	public String detailsOfPlot(@LoggedIn Optional<UserAccount> user, @PathVariable Plot plot, Model model) {
		if (user.isEmpty()) {
			return detailsOfFreePlot(plot, model);
		}
		return securePlotController.detailsOfPlot(user.get(), plot, model);
	}

	/**
	 * Create model with general information of a {@link Plot} to show the detail page of a {@link Plot} when no user
	 * is logged in
	 *
	 * @param plot {@link Plot} of which general information should be shown
	 * @param mav  {@link Model} to add needed information
	 * @return view as {@link String}
	 */
	public String detailsOfFreePlot(@PathVariable Plot plot, Model mav) {
		Set<PlotInformationBuffer> plotsToShow = new HashSet<>();

		try {
			if (plot.getStatus() == PlotStatus.TAKEN) {
				mav.addAttribute("error", "Unauthenticated user must not have access to a rented plot!");
				return "error";
			}
			plotsToShow.add(new PlotInformationBuffer(plot));
		} catch (Exception e) {
			mav.addAttribute("error", e);
			return "error";
		}
		mav.addAttribute("plots", plotsToShow);
		return "plot/myPlot";
	}

	/**
	 * Create model with needed information to show the overview of all {@link Plot}s when no user is logged in
	 *
	 * @return response as {@link ModelAndView}
	 */
	public ModelAndView insecurePlotOverview() {
		ModelAndView mav = new ModelAndView();

		List<Plot> plots = plotCatalog.getAll().toList();
		Map<Plot, String> colors = new HashMap<>();
		Map<Plot, Boolean> rights = new HashMap<>();
		Map<String, String> usedColors = new HashMap<>();

		for (Plot plot : plots) {
			Map<Plot, String> colorOfPlot = plotControllerService.setPlotColor(plot, Optional.empty());
			Map<Plot, Boolean> accessForPlot = plotControllerService.setAccessRightForPlot(plot, Optional.empty());

			colors.put(plot, colorOfPlot.get(plot));
			rights.put(plot, accessForPlot.get(plot));
		}

		usedColors.put("#7CB342", "frei");
		usedColors.put("#546E7A", "besetzt");

		mav.addObject("plotList", plots);
		mav.addObject("plotColors", colors);
		mav.addObject("colors", usedColors);
		mav.addObject("userRights", rights);
		mav.setViewName("plot/plotOverview");

		return mav;
	}

}
