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

@Component
public class WorkAssignmentTimer {

	private final WorkAssignmentManager workAssignmentManager;
	private final TenantManager tenantManager;
	private final DataService dataService;

	public WorkAssignmentTimer(WorkAssignmentManager workAssignmentManager, TenantManager tenantManager, DataService dataService){
		this.workAssignmentManager = workAssignmentManager;
		this.tenantManager = tenantManager;
		this.dataService = dataService;

		System.out.println(workAssignmentManager);
		System.out.println(tenantManager);
		System.out.println(dataService);
	}

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

	public String getPeriod(UserAccount userAccount){
		List<LocalDateTime> dateTimeList = new LinkedList<>();
		for(Plot plot : dataService.getRentedPlots(tenantManager.getTenantByUserAccount(userAccount))){
			for (WorkAssignment workAssignment : workAssignmentManager.getForPlotWorkAssignments(plot.getId())){
				if(workAssignment.getDate().isAfter(LocalDateTime.now())){
					dateTimeList.add(workAssignment.getDate());
				}
			}
		}
		if(dateTimeList.size() > 0) {
			dateTimeList.sort(LocalDateTime::compareTo);
			return timeDifference(LocalDateTime.now(), dateTimeList.get(0));
		}
		return "no pending assignments";
	}
}
