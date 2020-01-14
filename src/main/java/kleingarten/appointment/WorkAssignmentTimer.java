package kleingarten.appointment;

import kleingarten.plot.DataService;
import kleingarten.plot.Plot;
import kleingarten.tenant.TenantManager;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkAssignmentTimer {

	private final WorkAssignmentManager workAssignmentManager;
	private final TenantManager tenantManager;
	private final DataService dataService;

	/**
	 * Constructor for the WorkAssingmentTimer
	 *
	 * @param workAssignmentManager manager class of {@link WorkAssignment}s
	 * @param tenantManager         manager class of {@link kleingarten.tenant.Tenant}s
	 * @param dataService           service class of the plot package to work with {@link kleingarten.finance.Procedure}s
	 */
	public WorkAssignmentTimer(WorkAssignmentManager workAssignmentManager, TenantManager tenantManager,
							   DataService dataService) {

		this.workAssignmentManager = workAssignmentManager;
		this.tenantManager = tenantManager;
		this.dataService = dataService;

		System.out.println(workAssignmentManager);
		System.out.println(tenantManager);
		System.out.println(dataService);
	}

	/**
	 * Get difference between the actual time and a given time
	 *
	 * @param from actual time as {@link LocalDateTime}
	 * @param to   {@link LocalDateTime} for which the difference of time from now should be calculated
	 * @return String which contains the timer until the given time (to) of time as days, hours and minutes
	 */
	public static String timeDifference(LocalDateTime from, LocalDateTime to) {
		Long days, hours, minutes;
		days = from.until(to, ChronoUnit.DAYS);
		to = to.minusDays(days);
		hours = from.until(to, ChronoUnit.HOURS);
		to = to.minusHours(hours);
		minutes = from.until(to, ChronoUnit.MINUTES);
		return String.format("%dd %d:%02d", days, hours, minutes);
	}

	/**
	 * Get timer from now to the next {@link WorkAssignment} a user with the given {@link UserAccount} participates in
	 *
	 * @param userAccount {@link UserAccount} of the user for which a timer should be calculated
	 * @return timer for the next {@link WorkAssignment}
	 */
	public String getPeriod(UserAccount userAccount) {
		SortedMap<LocalDateTime, List<WorkAssignment>> assignmentMap = new TreeMap<>();
		for (Plot plot : dataService.getRentedPlots(tenantManager.getTenantByUserAccount(userAccount))) {
			for (WorkAssignment workAssignment : workAssignmentManager.getForPlotWorkAssignments(plot.getId())) {
				if (workAssignment.getDate().isAfter(LocalDateTime.now())) {
					if (!assignmentMap.containsKey(workAssignment.getDate())) {
						assignmentMap.put(workAssignment.getDate(), new LinkedList<>());
					}
					assignmentMap.get(workAssignment.getDate()).add(workAssignment);
				}
			}
		}
		try {
			String timeSpan = timeDifference(LocalDateTime.now(), assignmentMap.firstKey());
			List<WorkAssignment> assignments = assignmentMap.get(assignmentMap.firstKey());
			return assignments.stream().map(n -> " Zeit: " + timeSpan + " | Titel: " + n.getTitle() + " | Parzellenanzahl: " +
					n.getPlots().size()).collect(Collectors.joining(","));
		} catch (NoSuchElementException e) {
			return null;
		}
	}
}
