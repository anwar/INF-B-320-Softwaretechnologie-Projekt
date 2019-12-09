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
	private final PlotCatalog plotCatalog;

	PlotControllerService(DataService dataService, TenantManager tenantManager, PlotCatalog plotCatalog) {
		this.dataService = dataService;
		this.tenantManager = tenantManager;
		this.plotCatalog = plotCatalog;
	}

	/**
	 * Select color of a {@link Plot} when no user is logged in and save the selection in a {@link Map} of
	 * {@link Plot} and {@link String}
	 * @param plot {@link Plot} for which a color should be selected
	 * @param colors {@link Map} with {@link Plot}s and their selected colors as {@link String}
	 */
	void insecureSetPlotColor(Plot plot, Map<Plot, String> colors) {
		colors.put(plot, plot.getStatus() == PlotStatus.TAKEN ? "#546E7A" : "#7CB342");
	}

	/**
	 * Select color of a {@link Plot} when a user is logged in and save the selection in a {@link Map} of
	 * {@link Plot} and {@link String}
	 * @param plot {@link Plot} for which a color should be selected
	 * @param colors {@link Map} with {@link Plot}s and their selected colors as {@link String}
	 */
	void secureSetPlotColor(Plot plot, Map<Plot, String> colors) {
		colors.put(plot, plot.getStatus() == PlotStatus.TAKEN ? "#546E7A" : "#7CB342");

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
					colors.put(plot, "#E69138");
					return;
				}
				colors.put(plot, "#FDD835");
			} else if (rolesOfMainTenant.stream().anyMatch(chairman::contains)
				|| rolesOfSubTenants.stream().anyMatch(chairman::contains)) {
				colors.put(plot, "#039BE5");
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
	 * Set the color of a {@link Plot} depending on who is its chairman
	 * @return {@link Map} of {@link Plot}s and associated colors as {@link String}
	 */
	Map<Plot, String> secureSetColorOfChairmenForPlots() {
		List<String> colors = Arrays.asList("#7CB342", "#546E7A","039BE5", "#E06666", "E69138", "FDD835",
														"#6FA8DC", "#38761D", "#8E7CC3");
		Map<Tenant, String> colorsForChairman = new HashMap<>();
		int index = 1;
		for (Tenant chairman:
			 tenantManager.getAll()) {
			if (tenantManager.tenantHasRole(chairman, Role.of("Obmann"))) {
				colorsForChairman.put(chairman, colors.get(index));
				index += 1;
			}
		}

		Map<Plot, String> colorsForPlotAdministratedByChairman = new HashMap<>();
		for (Plot administratedPlot:
			 plotCatalog.findAll()) {
			for (Tenant chairman:
				 colorsForChairman.keySet()) {
				if (administratedPlot.getChairman() == chairman) {
					colorsForPlotAdministratedByChairman.put(administratedPlot, colorsForChairman.get(chairman));
				}
			}
			colorsForPlotAdministratedByChairman.put(administratedPlot, colors.get(0));
		}

		return colorsForPlotAdministratedByChairman;
	}
}
