package kleingarten.plot;

import com.querydsl.core.Tuple;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantManager;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Component;

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
	 * Select color of a {@link Plot} and save the selection in a {@link Map} of {@link Plot} and {@link String}
	 * @param plot {@link Plot} for which a color should be selected
	 * @param user {@link UserAccount} of the logged in user, if he is logged in
	 * @param colors {@link Map} with {@link Plot}s and their selected colors as {@link String}
	 */
	void setPlotColor(Plot plot, Optional<UserAccount> user, Map<Plot, String> colors) {
		colors.put(plot, plot.getStatus() == PlotStatus.TAKEN ? "grey" : "olive");

		if (user.isEmpty() || plot.getStatus() == PlotStatus.FREE) {
			return;
		}

		if (plot.getStatus() == PlotStatus.TAKEN) {
			Tenant mainTenant = tenantManager.get(dataService.getProcedure(LocalDateTime.now().getYear(), plot).getMainTenant());
			if (mainTenant == null) {
				throw new IllegalArgumentException("Tenant must not be null!");
			}

			if (tenantManager.tenantHasRole(mainTenant, Role.of("Vorstandsvorsitzender"))
				|| tenantManager.tenantHasRole(mainTenant, Role.of("Stellvertreter"))) {
				colors.put(plot, "yellow");
			} else if (tenantManager.tenantHasRole(mainTenant, Role.of("Kassierer"))) {
				colors.put(plot, "red");
			} else if (tenantManager.tenantHasRole(mainTenant, Role.of("Obmann"))) {
				colors.put(plot, "blue");
			}
		}
	}

	/**
	 * Select if a user can see the details page of a specific {@link Plot}
	 * @param plot {@link Plot} for which the access to the details page should be set
	 * @param user {@link UserAccount} of the logged in user, if he is logged in
	 * @param rights {@link Map} with {@link Plot}s and their kind of access as {@link Boolean} for the given user
	 */
	void setAccessRightForPlot(Plot plot, Optional<UserAccount> user, Map<Plot, Boolean> rights) {
		if (user.isEmpty()) {
			if (plot.getStatus() == PlotStatus.FREE) {
				rights.put(plot, true);
			} else {
				rights.put(plot, false);
			}
		} else {
			Tenant tenant = tenantManager.getTenantByUserAccount(user.get());
			if (tenant == null) {
				throw new IllegalArgumentException("Tenant must exist!");
			}

			List<Role> permitted = List.of(Role.of("Vorstandsvorsitzender"), Role.of("Stellvertreter"),
											Role.of("Protokollant"), Role.of("Kassierer"), Role.of("Wassermann"));
			boolean condition = user.get().getRoles().stream().anyMatch(permitted::contains);
			if (condition) {
				rights.put(plot, true);
			} else if (tenantManager.tenantHasRole(tenant, Role.of("Obmann"))) {
				rights.put(plot, plot.getChairman().getId() == tenant.getId());
			}
		}
	}
}
