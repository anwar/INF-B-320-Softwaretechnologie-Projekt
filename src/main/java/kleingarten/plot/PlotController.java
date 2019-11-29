package kleingarten.plot;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.format.number.money.MonetaryAmountFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.money.format.MonetaryFormats;
import java.util.Locale;
import java.util.Optional;

@Controller
public class PlotController {
	private final PlotService plotService;

	PlotController(PlotService plotService){
		this.plotService = plotService;
	}

	@GetMapping("/plot/{plot}")
	public ModelAndView detailsOfFreePlot(@LoggedIn Optional<UserAccount> user, @PathVariable Plot plot) {
		ModelAndView mav = new ModelAndView();
		plotService.getPlot(plot.getId());

		mav.addObject("plotName", plot.getName());
		mav.addObject("plotSize", plot.getSize() + " m²");
		mav.addObject("plotDescription", plot.getDescription());
		mav.addObject("plotPrice", MonetaryFormats.getAmountFormat(Locale.GERMANY).format(plot.getEstimator()));

		if (user.isPresent()) {
			throw new UnsupportedOperationException("Not implemented yet!");
		}
		else {
			mav.addObject("rented", false);
		}

		mav.setViewName("plot");
		return mav;
	}

}
