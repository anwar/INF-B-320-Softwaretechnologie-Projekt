package kleingarten.plot;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class PlotController {
	private final PlotService plotService;

	PlotController(PlotService plotService){
		//Assert.notNull(plotService,"PlotService must not be null");
		this.plotService = plotService;
	}
	
}
