package kleingarten.plot;

import kleingarten.Finance.Procedure;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.money.format.MonetaryFormats;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class PlotControllerService {
	private final DataService dataService;
	private final TenantManager tenantManager;
	private final PlotCatalog plotCatalog;
	private final PlotService plotService;

	PlotControllerService(DataService dataService, TenantManager tenantManager, PlotCatalog plotCatalog,
							PlotService plotService) {
		this.dataService = dataService;
		this.tenantManager = tenantManager;
		this.plotCatalog = plotCatalog;
		this.plotService = plotService;
	}

	/**
	 * Select color of a {@link Plot} when a user is logged in and save the selection in a {@link Map} of
	 * {@link Plot} and {@link String}
	 * @param plot {@link Plot} for which a color should be selected
	 * @return color of {@link Plot} saved in a {@link ModelAndView}
	 */
	Map<Plot, String> setPlotColor(Plot plot, Optional<UserAccount> user) {
		Map<Plot, String> colors = new HashMap<>();
		Set<Role> rolesOfMainTenant = new HashSet<>();
		Set<Role> rolesOfSubTenants = new HashSet<>();

		//Set default colors of plot if there is no user logged in
		colors.put(plot, plot.getStatus() == PlotStatus.TAKEN ? "#546E7A" : "#7CB342");

		if (user.isEmpty()) {
			return colors;
		}

		//Set color of plot if a user with role "Vorstand" or "Obmann" rents this plot
		if (plot.getStatus() == PlotStatus.TAKEN) {
			Tenant mainTenant = dataService.findTenantById(dataService.getProcedure(LocalDateTime.now().getYear(), plot)
											.getMainTenant());
			if (mainTenant == null) {
				throw new IllegalArgumentException("Tenant must not be null!");
			}
			//Get roles of mainTenant and subTenants
			Procedure procedure = dataService.getProcedure(LocalDateTime.now().getYear(), plot);
			rolesOfMainTenant.addAll(mainTenant.getUserAccount().getRoles().toSet());

			List<Tenant> subTenants = new LinkedList<>();
			for (long subTenantId:
					procedure.getSubTenants()) {
				subTenants.add(dataService.findTenantById(subTenantId));
			}
			for (Tenant subTenant:
					subTenants) {
				rolesOfSubTenants.addAll(subTenant.getUserAccount().getRoles().toSet());
			}

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
	 * Select if the details page of a specific {@link Plot} can be accessed depending on if a user is logged in or not
	 * @param plot {@link Plot} for which the access to the details page should be set
	 * @param user {@link Optional} of type {@link UserAccount} of the logged in user
	 * @return access rights saved in a {@link ModelAndView}
	 */
	Map<Plot, Boolean> setAccessRightForPlot(Plot plot, Optional<UserAccount> user) {
		Map<Plot, Boolean> rights = new HashMap<>();

		if (plot.getStatus() == PlotStatus.FREE) {
			rights.put(plot, true);
		} else {
			rights.put(plot, false);
		}

		//Check if user has a special role and set access rights depending on the roles
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
	 * Add information of a {@link Plot} to a {@link PlotInformationBuffer}
	 * @param procedure {@link Procedure} to which the {@link Plot} is associated
	 * @return information of {@link Plot} saved in a {@link PlotInformationBuffer}
	 */
	PlotInformationBuffer addInformationOfPlotToPlotInformationPuffer(Optional<Procedure> procedure, Plot plot) {
		PlotInformationBuffer buffer = new PlotInformationBuffer(plot);
		if (procedure.isEmpty()) {
			return buffer;
		} else {
			Map<Tenant, String> mainTenantInformation = new HashMap<>();
			Map<Tenant, String> subTenantsInformation = new HashMap<>();

			//Get information and roles of mainTenant
			Tenant mainTenant = dataService.findTenantById(procedure.get().getMainTenant());
			mainTenantInformation.put(mainTenant, mainTenant.getRoles());
			buffer.setMainTenantRole(mainTenantInformation);

			//Get information and roles of subTenants
			for (long tenantId : procedure.get().getSubTenants()) {
				Tenant tenant = dataService.findTenantById(tenantId);
				subTenantsInformation.put(tenant, tenant.getRoles());
			}
			buffer.setSubTenantRoles(subTenantsInformation);
			buffer.setWorkHours(procedure.get().getWorkMinutes() + "min");
		}
		return buffer;
	}

	/**
	 * Select which information a user can see and modify on the details page of a {@link Plot} when he is logged in
	 * @param procedure {@link Optional} of {@link Procedure} for the {@link Plot} which details should be shown
	 * @param tenant {@link Tenant} associated to the logged in {@link UserAccount}
	 * @param mav {@link Model} to save the access rights to the information of the {@link Plot}
	 */
	void secureSetAccessRightForPlotDetails(Optional<Procedure> procedure, Tenant tenant, Model mav) {
		List<Role> permitted = List.of(Role.of("Vorstandsvorsitzender"), Role.of("Stellvertreter"),
				Role.of("Protokollant"), Role.of("Kassierer"), Role.of("Wassermann"));
		boolean condition = tenant.getUserAccount().getRoles().stream().anyMatch(permitted::contains);

		//Check if user has a special role and set access rights for plot information
		if (condition) {
			if (dataService.tenantHasRole(tenant, Role.of("Kassierer"))) {
				mav.addAttribute("canSeeBills", true);
				mav.addAttribute("canSeeWorkhours", true);
				boolean condModify = dataService.tenantHasRole(tenant, Role.of("Wassermann"));
				mav.addAttribute("canModify", condModify);
			} else if (dataService.tenantHasRole(tenant, Role.of("Wassermann"))) {
				mav.addAttribute("canModify", true);
			} else if (dataService.tenantHasRole(tenant, Role.of("Vorstandsvorsitzender"))
						|| dataService.tenantHasRole(tenant, Role.of("Stellvertreter"))) {
				mav.addAttribute("canSeeBills", true);
				mav.addAttribute("canSeeWorkhours", true);
				mav.addAttribute("canSeeApplications", true);
				mav.addAttribute("canModify", true);
				mav.addAttribute("canCancel", true);
			} else if (procedure.isPresent()) {
				if (tenant.getUserAccount().hasRole(Role.of("Obmann"))
						&& plotService.findById(procedure.get().getPlotId()).getChairman().getId() == tenant.getId()) {
					mav.addAttribute("canSeeWorkhours", true);
					mav.addAttribute("canModify", true);
				}
			}
		} else if (procedure.isPresent()) {
			if (procedure.get().isTenant(tenant.getId())) {
				mav.addAttribute("canSeeWorkhours", true);
				mav.addAttribute("canSeeBills", true);
				mav.addAttribute("canModify", true);
				mav.addAttribute("rents", true);
			}
		}
	}

	//ModelAndView secure
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

	/**
	 * Generate a random color for each {@link Tenant} with the {@link Role} "Obmann"
	 * @return {@link Map} of {@link Tenant}s and colors as {@link String}
	 */
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
