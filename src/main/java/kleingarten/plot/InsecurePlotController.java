package kleingarten.plot;

import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.Role;
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
import javax.money.format.MonetaryFormats;
import java.util.*;

@Controller
public class InsecurePlotController {
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;
	private final PlotControllerService plotControllerService;
	private final SecurePlotController securePlotController;

	InsecurePlotController(PlotService plotService, PlotCatalog plotCatalog,
						   PlotControllerService plotControllerService,
						   SecurePlotController securePlotController) {
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.plotControllerService = plotControllerService;
		this.securePlotController = securePlotController;
	}

	/**
	 * Use different methods to show the overview of all {@link Plot}s depending if a user is logged in or not
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
	 * @param user {@link Optional} of {@link UserAccount}, contains the user's {@link UserAccount} when he is logged in
	 * @param plot {@link Plot} to show the details of
	 * @return response as {@link ModelAndView}
	 */
	@GetMapping("/plot/{plot}")
	public ModelAndView detailsOfPlot(@LoggedIn Optional<UserAccount> user, @PathVariable Plot plot) {
		if (user.isEmpty()) {
			return detailsOfFreePlot(plot);
		}
		System.out.println(plot);
		return securePlotController.detailsOfPlot(user.get(), plot);
	}

	/**
	 * Create model with general information of a {@link Plot} to show the detail page of a {@link Plot} when no user
	 * is logged in
	 * @param plot {@link Plot} of which general information should be shown
	 * @return response as {@link ModelAndView}
	 */
	public ModelAndView detailsOfFreePlot(@PathVariable Plot plot) {
		ModelAndView mav = new ModelAndView();

		if (!plotService.existsByName(plot.getName())) {
			mav.addObject("error","Plot must exist!");
		}
		if (plot.getStatus() == PlotStatus.TAKEN) {
			mav.addObject("error","Unauthenticated user must not have access to a rented plot!");
		}

		mav.addObject("rented", false);
		mav.addObject("subTenants", new HashMap<Tenant, String>());
		mav.addObject("plots", new HashMap<Plot, String>());

		plotControllerService.addGeneralInformationOfPlot(plot, mav);
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
