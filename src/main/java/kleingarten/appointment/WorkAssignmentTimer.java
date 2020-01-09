package kleingarten.appointment;

import kleingarten.plot.DataService;
import kleingarten.plot.Plot;
import kleingarten.tenant.TenantManager;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkAssignmentTimer {

	private final WorkAssignmentManager workAssignmentManager;
	private final TenantManager tenantManager;
	private final DataService dataService;

	/**
	 * Constructor for the WorkAssingmentTimer
	 * @param workAssignmentManager
	 * @param tenantManager
	 * @param dataService
	 */
	public WorkAssignmentTimer(WorkAssignmentManager workAssignmentManager, TenantManager tenantManager, DataService dataService){
		this.workAssignmentManager = workAssignmentManager;
		this.tenantManager = tenantManager;
		this.dataService = dataService;

		System.out.println(workAssignmentManager);
		System.out.println(tenantManager);
		System.out.println(dataService);
	}

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	public static String timeDifference(LocalDateTime from, LocalDateTime to)
	{
		Long days, hours, minutes;
		days = Long.valueOf(from.until(to, ChronoUnit.DAYS));
		to = to.minusDays(days);
		hours = Long.valueOf(from.until(to, ChronoUnit.HOURS));
		to = to.minusHours(hours);
		minutes = Long.valueOf(from.until(to, ChronoUnit.MINUTES));
		return String.format("%dd %d:%02d", days, hours, minutes);
	}

	/**
	 * @param userAccount
	 * @return
	 */
	public String getPeriod(UserAccount userAccount){
		SortedMap<LocalDateTime, List<WorkAssignment>> assignmentMap = new TreeMap<>();
		for(Plot plot : dataService.getRentedPlots(tenantManager.getTenantByUserAccount(userAccount))){
			for (WorkAssignment workAssignment : workAssignmentManager.getForPlotWorkAssignments(plot.getId())){
				if(workAssignment.getDate().isAfter(LocalDateTime.now())){
					if(!assignmentMap.containsKey(workAssignment.getDate())){
						assignmentMap.put(workAssignment.getDate(), new LinkedList<>());
					}
					assignmentMap.get(workAssignment.getDate()).add(workAssignment);
				}
			}
		}
		try {
			String timeSpan = timeDifference(LocalDateTime.now(), assignmentMap.firstKey());
			List<WorkAssignment> assignments = assignmentMap.get(assignmentMap.firstKey());
			return assignments.stream().map(n -> timeSpan + ": " + n.getTitle()).collect(Collectors.joining(","));
		}catch (NoSuchElementException e){
			return null;
		}
	}
}
