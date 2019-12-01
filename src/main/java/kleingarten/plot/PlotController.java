package kleingarten.plot;

import kleingarten.tenant.Tenant;
import net.bytebuddy.asm.Advice;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.money.format.MonetaryFormats;
import java.util.*;

@Controller
public class PlotController {
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;

	PlotController(PlotService plotService, PlotCatalog plotCatalog){
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
	}

	@GetMapping("/plot/{plot}")
	public ModelAndView details(@LoggedIn Optional<UserAccount> user, @PathVariable Plot plot) {
		ModelAndView mav = new ModelAndView();
		if (!plotService.existsByName(plot.getName())) {
			throw new IllegalArgumentException("Plot must exist!");
		}

		mav.addObject("plotID", plot.getId());
		mav.addObject("plotName", plot.getName());
		mav.addObject("plotSize", plot.getSize() + " m²");
		mav.addObject("plotDescription", plot.getDescription());
		mav.addObject("plotPrice", MonetaryFormats.getAmountFormat(Locale.GERMANY).format(plot.getEstimator()));

		if (user.isPresent()) {
			//TODO Tenant tenant = user.getTenant(); (not implemented yet)
		}
		else {
			mav.addObject("rented", false);
		}

		mav.setViewName("plot");
		return mav;
	}

	@GetMapping("/anlage")
	public ModelAndView plotOverview(@LoggedIn Optional<UserAccount> user) {
		ModelAndView mav = new ModelAndView();

		Set<Plot> plots = plotCatalog.findAll().toSet();
		Map<Plot, String> colors = new HashMap<>();
		for (Plot plot: plots) {
			colors.put(plot, plot.getStatus() == PlotStatus.TAKEN ? "grey" : "olive");
		}
		mav.addObject("plotList", plots);
		mav.addObject("plotColors", colors);
		mav.setViewName("plotOverview");
		return mav;

	}

}
