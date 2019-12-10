package kleingarten.plot;

import com.querydsl.core.Tuple;

import kleingarten.finance.Procedure;
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
	private final PlotCatalog plotCatalog;

	PlotControllerService(DataService dataService, TenantManager tenantManager, PlotCatalog plotCatalog) {
		this.dataService = dataService;
		this.tenantManager = tenantManager;
		this.plotCatalog = plotCatalog;
	}

	/**
	 * Select color of a {@link Plot} when a user is logged in and save the selection in a {@link Map} of
	 * {@link Plot} and {@link String}
	 * @param plot {@link Plot} for which a color should be selected
	 */
	Map<Plot, String> setPlotColor(Plot plot, Optional<UserAccount> user) {
		Map<Plot, String> colors = new HashMap<>();

		//Set default colors of plot if there is no user logged in
		colors.put(plot, plot.getStatus() == PlotStatus.TAKEN ? "#546E7A" : "#7CB342");

		if (user.isEmpty()) {
			return colors;
		}

		//Set color of plot if a user with role "Vorstand" or "Obmann" rents this plot
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

			//Check if the maintenant or the subtenants of the plot have the role "Vorstand" or "Obmann"
			if (rolesOfMainTenant.stream().anyMatch(administration::contains)
				|| rolesOfSubTenants.stream().anyMatch(administration::contains)) {
				if (rolesOfMainTenant.stream().anyMatch(chairman::contains)
					|| rolesOfSubTenants.stream().anyMatch(chairman::contains)) {
					colors.put(plot, "#E69138");
					return colors;
				}
				colors.put(plot, "#FDD835");
			} else if (rolesOfMainTenant.stream().anyMatch(chairman::contains)
				|| rolesOfSubTenants.stream().anyMatch(chairman::contains)) {
				colors.put(plot, "#039BE5");
			}
		}
		return colors;
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
	 * Select if the details page of a specific {@link Plot} can be accessed depending on if a user is logged in or not
	 * @param plot {@link Plot} for which the access to the details page should be set
	 * @param user {@link Optional} of type {@link UserAccount} of the logged in user
	 */
	Map<Plot, Boolean> setAccessRightForPlot(Plot plot, Optional<UserAccount> user) {
		Map<Plot, Boolean> rights = new HashMap<>();

		if (plot.getStatus() == PlotStatus.FREE) {
			rights.put(plot, true);
		} else {
			rights.put(plot, false);
		}

		if (user.isPresent()) {
			Tenant tenant = tenantManager.getTenantByUserAccount(user.get());

			List<Role> permitted = List.of(Role.of("Vorstandsvorsitzender"), Role.of("Stellvertreter"),
					Role.of("Protokollant"), Role.of("Kassierer"), Role.of("Wassermann"));
			boolean condition = user.get().getRoles().stream().anyMatch(permitted::contains);
			if (condition) {
				rights.put(plot, true);
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Obmann"))) {
				rights.put(plot, plot.getChairman().getId() == tenant.getId());
			}
		}
		return rights;
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
	 * Add information of a rented {@link Plot} to a {@link ModelAndView}
	 * @param procedure {@link Procedure} to which the {@link Plot} is associated
	 * @param tenant associated {@link Tenant} to the {@link UserAccount} who wants to access the details of the
	 * 				{@link Plot}
	 * @param mav {@link ModelAndView} to save the information of the {@link Plot}
	 */
	void addProcedureInformationOfPlot(Procedure procedure, Tenant tenant, ModelAndView mav) {
		Map<Tenant, String>  mainTenant = secureGetInformationOfMainTenant(procedure);
		Map<Tenant, String> subTenants = secureGetInformationOfSubTenants(procedure);

		for (Tenant rents:
			 mainTenant.keySet()) {
			mav.addObject("mainTenant", rents);
			mav.addObject("mainTenantRoles", mainTenant.get(rents));
		}

		mav.addObject("workHours", procedure.getWorkMinutes() + "min");
		mav.addObject("subTenants", subTenants);
	}

	/**
	 * Select which information a user can see on the details page of a {@link Plot} when he is logged in
	 * @param procedure {@link Optional} of {@link Procedure} for the {@link Plot} which details should be shown
	 * @param tenant {@link Tenant} associated to the logged in {@link UserAccount}
	 * @param mav {@link ModelAndView} to save the access rights to the information of the {@link Plot}
	 */
	void secureSetAccessRightForPlotDetails(Optional<Procedure> procedure, Tenant tenant, ModelAndView mav) {
		List<Role> permitted = List.of(Role.of("Vorstandsvorsitzender"), Role.of("Stellvertreter"),
				Role.of("Protokollant"), Role.of("Kassierer"), Role.of("Wassermann"));
		boolean condition = tenant.getUserAccount().getRoles().stream().anyMatch(permitted::contains);
		if (condition) {
			mav.addObject("canSeeWorkhours", true);
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
				mav.addObject("canCancel", true);
			}
		}
		procedure.ifPresent(value -> secureSetAccessRightForTenantOfPlot(value, tenant, mav));
	}

	/**
	 * Select which information a user can see on the details page of a {@link Plot} when he is logged in
	 * @param procedure {@link Procedure} for the {@link Plot} which details should be shown
	 * @param tenant {@link Tenant} associated to the logged in {@link UserAccount}
	 * @param modelAndView {@link ModelAndView} to save the access rights to the information of the {@link Plot}
	 */
	void secureSetAccessRightForTenantOfPlot(Procedure procedure, Tenant tenant, ModelAndView modelAndView) {
		if (procedure.isTenant(tenant.getId())) {
			modelAndView.addObject("canSeeWorkhours", true);
			modelAndView.addObject("canSeeBills", true);
			modelAndView.addObject("canSeeApplications", true);
			modelAndView.addObject("canModify", true);
			modelAndView.addObject("rents", true);
			if (procedure.getMainTenant() == tenant.getId()) {
				modelAndView.addObject("canCancel", true);
			}
		}
	}

	/**
	 * Get information for mainTenant who rents a {@link Plot}
	 * @param procedure {@link Procedure} to which the {@link Plot} is associated to
	 * @return {@link Map} with the {@link Tenant} and his {@link Role}s as {@link String}
	 */
	Map<Tenant, String> secureGetInformationOfMainTenant(Procedure procedure) {
		Map<Tenant, String> mainTenantInfo = new HashMap<>();
		Tenant tenant = dataService.findTenantById(procedure.getMainTenant());
		mainTenantInfo.put(tenant, tenant.getRoles());
		return mainTenantInfo;
	}

	/**
	 * Get information for subTenants who rent a {@link Plot}
	 * @param procedure {@link Procedure} to which the {@link Plot} is associated to
	 * @return {@link Map} with the {@link Tenant}s and their {@link Role}s as {@link String}
	 */
	Map<Tenant, String> secureGetInformationOfSubTenants(Procedure procedure) {
		Map<Tenant, String> subTenantsInfo = new HashMap<>();
		for (long tenantId : procedure.getSubTenants()) {
			Tenant tenant = dataService.findTenantById(tenantId);
			subTenantsInfo.put(tenant, tenant.getRoles());
		}
		return subTenantsInfo;
	}

	/**
	 * Get information of the {@link Plot}s a {@link Tenant} rents and save this information
	 * @param tenant {@link Tenant} who's rented {@link Plot}s should be saved
	 * @param rentedPlots {@link Map} of the {@link Plot}s the {@link Tenant} rents and their names as {@link String}
	 */
	void secureGetRentedPlotsForTenant(Tenant tenant, Map<Plot, String> rentedPlots) {
		Set<Plot> plots = dataService.getRentedPlots(LocalDateTime.now().getYear(), tenant);
		for (Plot parcel : plots) {
			rentedPlots.put(parcel, parcel.getName());
		}
	}

	/**
	 * Set the color of a {@link Plot} depending on who is its chairman of type {@link Tenant} and add the
	 * @return {@link Map} of {@link Plot}s and associated colors as {@link String}
	 */
	Map<Plot, String> secureSetColorOfChairmenForPlots() {
		//Set color for each tenant with the role "Obmann"
		Map<Tenant, String> colorsForChairmen = secureSetColorForChairman();

		//Set color for plot depending on who is its chairman
		Map<Plot, String> colorsForPlotAdministratedByChairman = new HashMap<>();
		for (Plot administratedPlot:
			 plotCatalog.findAll()) {
			for (Tenant chairman:
				 colorsForChairmen.keySet()) {
				if (administratedPlot.getChairman() == null) {
					colorsForPlotAdministratedByChairman.put(administratedPlot, "#7CB342");
				} else if (administratedPlot.getChairman() == chairman) {
					colorsForPlotAdministratedByChairman.put(administratedPlot, colorsForChairmen.get(chairman));
				}
			}
		}
		return colorsForPlotAdministratedByChairman;
	}

	Map<Tenant, String> secureSetColorForChairman() {
		Random randomColorGenerator = new Random();
		Map<Tenant, String> colorsForChairman = new HashMap<>();

		//Add random color for each tenant who has the role "Obmann"
		for (Tenant chairman:
				tenantManager.getAll()) {
			if (tenantManager.tenantHasRole(chairman, Role.of("Obmann"))) {
				int color = randomColorGenerator.nextInt(0x1000000);
				colorsForChairman.put(chairman, String.format("#%06X", color));
			}
		}
		return colorsForChairman;
	}
}
