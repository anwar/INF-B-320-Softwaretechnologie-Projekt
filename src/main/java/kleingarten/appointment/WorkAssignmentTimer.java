package kleingarten.appointment;

import kleingarten.plot.DataService;
import kleingarten.plot.Plot;
import kleingarten.tenant.TenantManager;
import org.salespointframework.useraccount.UserAccount;

import java.time.LocalDateTime;

public class WorkAssignmentTimer {

	private WorkAssignmentManager workAssignmentManager;
	private TenantManager tenantManager;
	private DataService dataService;

	private WorkAssignmentTimer(){}

	public WorkAssignmentTimer(WorkAssignmentManager workAssignmentManager, TenantManager tenantManager, DataService dataService){
		this.workAssignmentManager = workAssignmentManager;
		this.tenantManager = tenantManager;
		this.dataService = dataService;
	}

	public String getPeriod(UserAccount userAccount){
		for(Plot plot : dataService.getRentedPlots(tenantManager.getTenantByUserAccount(userAccount))){
			for (WorkAssignment workAssignment : workAssignmentManager.getForPlotWorkAssignments(plot.getId())){
				if(workAssignment.getDate().isAfter(LocalDateTime.now())){


				}
			}

		}
		return LocalDateTime.now().toString();
	}
}
