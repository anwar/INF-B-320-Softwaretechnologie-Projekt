package kleingarten.plot;

import com.sun.xml.bind.v2.TODO;
import kleingarten.Finance.Procedure;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.money.format.MonetaryFormats;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class PlotController {
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;
	private final TenantRepository tenantRepository;
	private final DataService dataService;

	PlotController(PlotService plotService, PlotCatalog plotCatalog, TenantRepository tenantRepository, DataService dataService) {
		this.plotService = plotService;
		this.plotCatalog = plotCatalog;
		this.tenantRepository = tenantRepository;
		this.dataService = dataService;
	}

	@GetMapping("/plot/{plot}")
	public ModelAndView details(@LoggedIn Optional<UserAccount> user, @PathVariable Plot plot, LocalDateTime time) {
		ModelAndView mav = new ModelAndView();
		if (!plotService.existsByName(plot.getName())) {
			throw new IllegalArgumentException("Plot must exist!");
		}

		//Procedure procedure = plotService.getProcedure(time.getYear(), plot.getId());
		//if (procedure == null) {
		//	throw new IllegalArgumentException("Procedure must exist!");
		//}

		// Add default information to the model
		mav.addObject("plotID", plot.getId());
		mav.addObject("plotName", plot.getName());
		mav.addObject("plotSize", plot.getSize() + " m²");
		mav.addObject("plotDescription", plot.getDescription());
		mav.addObject("plotPrice", MonetaryFormats.getAmountFormat(Locale.GERMANY).format(plot.getEstimator()));
		//mav.addObject("mainTenant", plotService.compareTenants(procedure.getMainTenant()));

		//Set<Tenant> subTenants = new HashSet<>();
		//for (long tenantId:
		//	 procedure.getSubTenants()) {
		//	subTenants.add(plotService.findTenantById(tenantId));
		//}

		//mav.addObject("subTenants", subTenants);
		//mav.addObject("workHours", procedure.getWorkMinutes() + "min");

		mav.setViewName("plot");

		if (user.isPresent()) {
			if (tenantRepository.findByUserAccount(user.get()).isEmpty()) {
				throw new IllegalArgumentException("User must exist!");
			}
			Tenant tenant = tenantRepository.findByUserAccount(user.get()).get();
			//if (plotService.compareTenants(tenant.getId())) {
			//if (procedure.isTenant(tenant.getId())) {
			// todo
			//	ModelAndView mav = new ModelAndView("redirect:/myPlot");
			//  return mav;
			//}

			mav.addObject("rented", true);
			// Check which role the user has to provide the correct rights of access on the view
			if (dataService.tenantHasRole(tenant, Role.of("Protokollant"))) {
				mav.addObject("canSee", true);
				mav.addObject("canSeeBills", false);
				mav.addObject("canSeeApplications", false);
				mav.addObject("canModify", false);
			}
			else if (dataService.tenantHasRole(tenant, Role.of("Kassierer"))) {
				mav.addObject("canSee", true);
				mav.addObject("canSeeBills", true);
				mav.addObject("canSeeApplications", false);
				boolean condModify = dataService.tenantHasRole(tenant, Role.of("Obmann"))
					               || dataService.tenantHasRole(tenant, Role.of("Wassermann"));
				mav.addObject("canModify", condModify);

			}
			else if (dataService.tenantHasRole(tenant, Role.of("Obmann")) || dataService.tenantHasRole(tenant, Role.of("Wassermann"))) {
				mav.addObject("canSee", true);
				mav.addObject("canSeeBills", false);
				mav.addObject("canSeeApplications", false);
				mav.addObject("canModify", true);
			}
			else if (dataService.tenantHasRole(tenant, Role.of("Vorstandsvorsitzender")) || dataService.tenantHasRole(tenant, Role.of("Stellvertreter"))) {
				mav.addObject("canSee", true);
				mav.addObject("canSeeBills", true);
				mav.addObject("canSeeApplications", true);
				mav.addObject("canModify", true);
			}
		}
		// User is not logged in and should only see information of a free plot
		else {
			if (plot.getStatus() == PlotStatus.TAKEN) {
				throw new IllegalArgumentException("Unauthenticated user must not have access to a rented plot!");
			}
			mav.addObject("rented", false);
		}

		return mav;
	}

	@GetMapping("/myPlot")
	public ModelAndView rentedPlots(@LoggedIn UserAccount user, Plot plot) {
		ModelAndView mav = new ModelAndView();
		if (!plotService.existsByName(plot.getName())) {
			throw new IllegalArgumentException("Plot must exist!");
		}

		mav.setViewName("myPlot");

		//Procedure procedure = plotService.getProcedure(time.getYear(), plot.getId());
		//if (procedure == null) {
		//	throw new IllegalArgumentException("Procedure must exist!");
		//}

		// Add default information to the model
		mav.addObject("plotID", plot.getId());
		mav.addObject("plotName", plot.getName());
		mav.addObject("plotSize", plot.getSize() + " m²");
		mav.addObject("plotDescription", plot.getDescription());
		mav.addObject("plotPrice", MonetaryFormats.getAmountFormat(Locale.GERMANY).format(plot.getEstimator()));
		//mav.addObject("mainTenant", plotService.compareTenants(procedure.getMainTenant()));

		//Set<Tenant> subTenants = new HashSet<>();
		//for (long tenantId:
		//	 procedure.getSubTenants()) {
		//	subTenants.add(plotService.findTenantById(tenantId));
		//}

		//mav.addObject("subTenants", subTenants);
		//mav.addObject("workHours", procedure.getWorkMinutes() + "min");

		if (tenantRepository.findByUserAccount(user).isEmpty()) {
			throw new IllegalArgumentException("User must exist!");
		}
		Tenant tenant = tenantRepository.findByUserAccount(user).get();
		//if (plotService.compareTenants(tenant.getId())) {
		//if (procedure.isTenant(tenant.getId()))
		return mav;
	}

	@GetMapping("/anlage")
	public ModelAndView plotOverview(@LoggedIn Optional<UserAccount> user) {
		ModelAndView mav = new ModelAndView();

		Set<Plot> plots = plotCatalog.findAll().toSet();
		Map<Plot, String> colors = new HashMap<>();
		Map<Plot, Boolean> rights = new HashMap<>();

		for (Plot plot : plots) {
			colors.put(plot, plot.getStatus() == PlotStatus.TAKEN ? "grey" : "olive");
			if (plot.getStatus() == PlotStatus.FREE) {
				rights.put(plot, true);
			}
			else {
				Tenant mainTenant = dataService.findTenantById(dataService.getProcedure(LocalDateTime.now().getYear(), plot).getMainTenant());
				if (dataService.tenantHasRole(mainTenant, Role.of("Vorstandsvorsitzender")) || dataService.tenantHasRole(mainTenant, Role.of("Stellvertreter"))) {
					colors.put(plot, "yellow");
				}
				else if (dataService.tenantHasRole(mainTenant, Role.of("Kassierer"))) {
					colors.put(plot, "red");
				}
				else if (dataService.tenantHasRole(mainTenant, Role.of("Obmann"))) {
					colors.put(plot, "blue");
				}
				rights.put(plot, false);
			}
			if (user.isPresent()) {
				if (tenantRepository.findByUserAccount(user.get()).isEmpty()) {
					throw new IllegalArgumentException("User must exist!");
				}
				Tenant tenant = tenantRepository.findByUserAccount(user.get()).get();
				if (dataService.tenantHasRole(tenant, Role.of("Vorstandsvorsitzender"))
					|| dataService.tenantHasRole(tenant, Role.of("Stellvertreter"))) {
					rights.put(plot, true);
					mav.addObject("canAdd", true);
				}
				else if (dataService.tenantHasRole(tenant, Role.of("Protokollant"))
					|| dataService.tenantHasRole(tenant, Role.of("Kassierer"))
					|| dataService.tenantHasRole(tenant, Role.of("Wassermann"))) {
					rights.put(plot, true);
				}
				//TODO Implement usage of chairman
				/*else if (dataService.tenantHasRole(tenant, Role.of("Obmann"))) {
					rights.put(plot, plot.getChairman().getId() == tenant.getId());
				}*/
			}
		}

		mav.addObject("plotList", plots);
		mav.addObject("plotColors", colors);
		mav.addObject("userRights", rights);
		mav.setViewName("plotOverview");

		return mav;

	}


}
