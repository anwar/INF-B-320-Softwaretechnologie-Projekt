package kleingarten.appointment;

import kleingarten.plot.PlotDataInitializer;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class AppointmentInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(AppointmentInitializer.class);
	private final AppointmentRepository appointmentRepository;
	private final AppointmentManager appointmentManager;


	public AppointmentInitializer(AppointmentRepository appointmentRepository, AppointmentManager appointmentManager){
		Assert.notNull(appointmentRepository, "AppointmentRepository must not be null!");
		Assert.notNull(appointmentManager, "AppointmentManager must not be null!");
		this.appointmentRepository = appointmentRepository;
		this.appointmentManager = appointmentManager;
	}

	@Override
	public void initialize() {
		if (!this.appointmentManager.getAll().toList().isEmpty()) {
			return;
		}
		LOG.info("create default Appointments");
		var Appointment = this.appointmentManager.createAppointmentForInitializer(LocalTime.now(), LocalDate.now());
		var Appointment1 = this.appointmentManager.createAppointmentForInitializer(LocalTime.of(18,29), LocalDate.of(2020,12,29));


		this.appointmentRepository.saveAll(List.of(Appointment, Appointment1));
	}
}
