package kleingarten.plot;

import com.querydsl.core.Tuple;
import kleingarten.Finance.Procedure;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.money.format.MonetaryFormats;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class PlotControllerService {
	private final DataService dataService;
	private final TenantManager tenantManager;

	PlotControllerService(DataService dataService, TenantManager tenantManager) {
		this.dataService = dataService;
		this.tenantManager = tenantManager;
	}

	/**
	 * Select color of a {@link Plot} when no user is logged in and save the selection in a {@link Map} of
	 * {@link Plot} and {@link String}
	 * @param plot {@link Plot} for which a color should be selected
	 * @param colors {@link Map} with {@link Plot}s and their selected colors as {@link String}
	 */
	void insecureSetPlotColor(Plot plot, Map<Plot, String> colors) {
		colors.put(plot, plot.getStatus() == PlotStatus.TAKEN ? "grey" : "olive");
	}

	/**
	 * Select color of a {@link Plot} when a user is logged in and save the selection in a {@link Map} of
	 * {@link Plot} and {@link String}
	 * @param plot {@link Plot} for which a color should be selected
	 * @param colors {@link Map} with {@link Plot}s and their selected colors as {@link String}
	 */
	void secureSetPlotColor(Plot plot, Map<Plot, String> colors) {
		colors.put(plot, plot.getStatus() == PlotStatus.TAKEN ? "grey" : "olive");

		if (plot.getStatus() == PlotStatus.TAKEN) {
			Tenant mainTenant = tenantManager.get(dataService.getProcedure(LocalDateTime.now().getYear(), plot)
											.getMainTenant());
			if (mainTenant == null) {
				throw new IllegalArgumentException("Tenant must not be null!");
			}

			Set<Role> rolesOfSubTenants = getRolesOfSubTenants(plot);
			Set<Role> rolesOfMainTenant = getRolesOfTenant(plot);
			Set<Role> administration = Set.of(Role.of("Vorstandsvorsitzender"), Role.of("Stellvertreter"));
			Set<Role> chairman = Set.of(Role.of("Obmann"));

			if (rolesOfMainTenant.stream().anyMatch(administration::contains)
				|| rolesOfSubTenants.stream().anyMatch(administration::contains)) {
				if (rolesOfMainTenant.stream().anyMatch(chairman::contains)
					|| rolesOfSubTenants.stream().anyMatch(chairman::contains)) {
					colors.put(plot, "orange");
					return;
				}
				colors.put(plot, "yellow");
			} else if (rolesOfMainTenant.stream().anyMatch(chairman::contains)
				|| rolesOfSubTenants.stream().anyMatch(chairman::contains)) {
				colors.put(plot, "blue");
			}
		}
	}

	/**
	 * Get the {@link Role}s of the mainTenant of a {@link Plot} as {@link Set} of {@link Role}
	 * @param plot {@link Plot} for which the {@link Role}s of the mainTenant should be returned
	 * @return {@link Role}s of the mainTenant as {@link Set} of {@link Role}
	 */
	Set<Role> getRolesOfTenant(Plot plot) {
		Procedure procedure = dataService.getProcedure(LocalDateTime.now().getYear(), plot);
		Tenant mainTenant = dataService.findTenantById(procedure.getMainTenant());
		return mainTenant.getUserAccount().getRoles().toSet();
	}

	/**
	 * Get the {@link Role}s of the subTenants of a {@link Plot} as {@link Set} of {@link Role}
	 * @param plot {@link Plot} for which the {@link Role}s of the subTenants should be returned
	 * @return {@link Role}s of the subTenants as {@link Set} of {@link Role}
	 */
	Set<Role> getRolesOfSubTenants(Plot plot) {
		Procedure procedure = dataService.getProcedure(LocalDateTime.now().getYear(), plot);
		List<Tenant> subTenants = new LinkedList<>();
		Set<Role> subTenantRoles = new HashSet<>();

		for (long subTenantId:
				procedure.getSubTenants()) {
			subTenants.add(dataService.findTenantById(subTenantId));
		}
		for (Tenant subTenant:
			 subTenants) {
			subTenantRoles.addAll(subTenant.getUserAccount().getRoles().toSet());
		}
		return subTenantRoles;
	}

	/**
	 * Select if details page of a specific {@link Plot} can be accessed when no user is logged in
	 * @param plot {@link Plot} for which the access to the details page should be set
	 * @param rights {@link Map} with {@link Plot}s and their kind of access as {@link Boolean}
	 */
	void insecureSetAccessRightForPlot(Plot plot, Map<Plot, Boolean> rights) {
		if (plot.getStatus() == PlotStatus.FREE) {
			rights.put(plot, true);
		} else {
			rights.put(plot, false);
		}
	}

	/**
	 * Select if a user can see the details page of a specific {@link Plot} when he is logged in
	 * @param plot {@link Plot} for which the access to the details page should be set
	 * @param user {@link UserAccount} of the logged in user
	 * @param rights {@link Map} with {@link Plot}s and their kind of access as {@link Boolean} for the given user
	 */
	void secureSetAccessRightForPlot(Plot plot, UserAccount user, Map<Plot, Boolean> rights) {
		Tenant tenant = tenantManager.getTenantByUserAccount(user);

		List<Role> permitted = List.of(Role.of("Vorstandsvorsitzender"), Role.of("Stellvertreter"),
					Role.of("Protokollant"), Role.of("Kassierer"), Role.of("Wassermann"));
		boolean condition = user.getRoles().stream().anyMatch(permitted::contains);
		if (condition) {
			rights.put(plot, true);
		} else if (tenantManager.tenantHasRole(tenant, Role.of("Obmann"))) {
			rights.put(plot, plot.getChairman().getId() == tenant.getId());
		}
	}

	/**
	 * Add general information of a {@link Plot} to a {@link ModelAndView}
	 * @param plot {@link Plot} of which the general information should be used
	 * @param mav {@link ModelAndView} to save the information of the {@link Plot}
	 */
	void addGeneralInformationOfPlot(Plot plot, ModelAndView mav) {
		mav.addObject("plot", plot);
		mav.addObject("plotID", plot.getId());
		mav.addObject("plotName", plot.getName());
		mav.addObject("plotSize", plot.getSize() + " m²");
		mav.addObject("plotDescription", plot.getDescription());
		mav.addObject("plotPrice", MonetaryFormats.getAmountFormat(Locale.GERMANY)
				.format(plot.getEstimator()));
	}

	/**
	 * Select which information a user can see on the details page of a {@link Plot} when he is logged in
	 * @param plot {@link Plot} for which the access to the information should be set
	 * @param tenant {@link UserAccount} of the logged in user
	 * @param mav {@link ModelAndView} to save the access rights to the information of the {@link Plot}
	 */
	void secureSetAccessRightForPlotDetails(Plot plot, Tenant tenant, ModelAndView mav) {
		mav.addObject("canSee", true);

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
	}

	/**
	 * Get information for a rented {@link Plot}
	 * @param procedure {@link Procedure} to which the {@link Plot} is associated to
	 * @param mav {@link ModelAndView} to save the information of the rented {@link Plot}
	 */
	void secureGetInformationOfProcedure(Procedure procedure, ModelAndView mav) {
		mav.addObject("mainTenant", dataService.findTenantById(procedure.getMainTenant()));
		Set<Tenant> subTenants = new HashSet<>();
		for (long tenantId : procedure.getSubTenants()) {
			subTenants.add(dataService.findTenantById(tenantId));
		}
		mav.addObject("workHours", procedure.getWorkMinutes() + "min");
	}
}
