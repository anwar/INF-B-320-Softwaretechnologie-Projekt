package kleingarten.appointment;

import com.mysema.commons.lang.Assert;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WorkAssignmentInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(WorkAssignmentInitializer.class);

	private final WorkAssignmentRepository workAssignmentRepository;
	private final WorkAssignmentManager workAssignmentManager;


	public WorkAssignmentInitializer(WorkAssignmentRepository workAssignmentRepository,WorkAssignmentManager workAssignmentManager){

		Assert.notNull(workAssignmentRepository, "WorkAssignmentRepository must not be null!");
		Assert.notNull(workAssignmentManager, "WorkAssignmentManager must not be null!");
		this.workAssignmentRepository = workAssignmentRepository;
		this.workAssignmentManager = workAssignmentManager;
	}

	@Override
	public void initialize() {
		if (!this.workAssignmentManager.getAll().isEmpty()) {
			return;
		}
		LOG.info("create default Assignments");
		var Appointment = this.workAssignmentManager.createAssignmentForInitializer(LocalDateTime.of(2020,1,22,15,20,10), "Garten putzen", "Garten sauber machen Yalah");
		this.workAssignmentRepository.saveAll(List.of(Appointment));
	}
}
