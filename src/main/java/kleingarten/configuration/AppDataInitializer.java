/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kleingarten.configuration;

import kleingarten.appointment.WorkAssignmentManager;
import kleingarten.appointment.WorkAssignmentRepository;
import kleingarten.finance.*;
import kleingarten.news.NewsEntry;
import kleingarten.news.NewsRepository;
import kleingarten.plot.Plot;
import kleingarten.plot.PlotCatalog;
import kleingarten.plot.PlotService;
import kleingarten.tenant.Tenant;
import kleingarten.tenant.TenantRepository;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A {@link DataInitializer} implementation that will create dummy data for the application
 * on application startup.
 */
@Component
class AppDataInitializer implements DataInitializer {
	private static final Logger LOG = LoggerFactory.getLogger(AppDataInitializer.class);

	private final NewsRepository news;
	private final WorkAssignmentRepository workAssignmentRepo;
	private final WorkAssignmentManager workAssignmentManager;
	private final TenantRepository tenantRepository;
	private final UserAccountManager userAccountManager;
	private final PlotService plotService;
	private final PlotCatalog plotCatalog;
	private final FeeRepository feeRepository;
	private final FeeManager feeManager;
	private final ProcedureManager procedureManager;

	AppDataInitializer(NewsRepository news,
					   WorkAssignmentRepository workAssignmentRepo, WorkAssignmentManager workAssignmentManager,
					   TenantRepository tenantRepository, UserAccountManager userAccountManager,
					   PlotService plotService, PlotCatalog plotCatalog,
					   FeeRepository feeRepository, FeeManager feeManager, ProcedureManager procedureManager) {
		Assert.notNull(news, "news must not be null!");
		this.news = news;

		Assert.notNull(workAssignmentRepo, "workAssignmentRepo must not be null!");
		this.workAssignmentRepo = workAssignmentRepo;
		Assert.notNull(workAssignmentManager, "workAssignmentManager must not be null!");
		this.workAssignmentManager = workAssignmentManager;

		Assert.notNull(tenantRepository, "tenantRepository must not be null!");
		this.tenantRepository = tenantRepository;
		Assert.notNull(userAccountManager, "userAccountManager must not be null!");
		this.userAccountManager = userAccountManager;

		Assert.notNull(plotService, "plotService must not be null!");
		this.plotService = plotService;
		Assert.notNull(plotCatalog, "plotCatalog must not be null!");
		this.plotCatalog = plotCatalog;

		Assert.notNull(feeRepository, "feeRepository must not be null!");
		this.feeRepository = feeRepository;
		Assert.notNull(feeManager, "feeManager must not be null!");
		this.feeManager = feeManager;
		Assert.notNull(procedureManager, "procedureManager must not be null!");
		this.procedureManager = procedureManager;
	}

	@Override
	public void initialize() {
		initializeNewsEntries(this.news);
		initializeWorkAssigment(this.workAssignmentRepo);
		initializeTenants(this.tenantRepository, this.userAccountManager);
		initializePlots(this.plotCatalog, this.plotService);
		initializeFee(this.feeRepository, this.feeManager);
		initializeProcedures(this.procedureManager);
		LOG.info("fertig");
	}

	/**
	 * Initializes dummy {@link NewsEntry}s on application startup.
	 */
	void initializeNewsEntries(NewsRepository news) {
		Assert.notNull(news, "News must not be null!");

		if (news.findAll().iterator().hasNext()) {
			return;
		}

		LOG.info("Creating default news entries...");

		news.save(new NewsEntry("Hört hört, werte Leute, am 13.09.2020 ist wieder Zeit für das jährliche Ritterfest im Lande K(l)einGarten. ⚔️"));
		news.save(new NewsEntry("Wiener Schnitzel. Ihr habt richtig gelesen. It's time for a d-d-duel. Schnitzelwettessen. Wo? Na hier natürlich. Wann? Am 30.02.2020. \uD83E\uDD69"));
		news.save(new NewsEntry("30.03.2019 Achtung: Bitte denkt daran, dass eure Stiefmütterchen auch wachsen können: Laub harken ist angesagt! \uD83C\uDF42 "));
	}


	void initializeWorkAssigment(WorkAssignmentRepository workAssignmentRepo) {
		Assert.notNull(workAssignmentRepo, "WorkAssignment must not be null");

		if (!this.workAssignmentManager.getAll().isEmpty()) {
			return;
		}
		LOG.info("create default Assignments");
		var Appointment = this.workAssignmentManager.createAssignmentForInitializer(LocalDateTime.of(2020, 1,1, 1, 0),0, "Neujahrsputz", "Garten von Böllerresten und Müll befreien", null);
		var Appointment2 = this.workAssignmentManager.createAssignmentForInitializer(LocalDateTime.of(2020, 4, 25,  16, 0), 0, "In K(l)einanlage Müll aufsammeln", "Müll aufheben", null);
		this.workAssignmentRepo.saveAll(List.of(Appointment));

	}

	void initializeTenants(TenantRepository tenantRepository, UserAccountManager userAccountManager) {
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(tenantRepository, "TenantRepository must not be null");

		if (userAccountManager.findByUsername("peter.klaus").isPresent()) {
			return;
		}

		LOG.info("Creating default users and customers");
		LOG.info("Habe gleich fertig");

		var password = Password.UnencryptedPassword.of("123");

		var vorstandRole = Role.of("Vorstandsvorsitzender");
		var protocolRole = Role.of("Protokollant");
		var stellvertreterRole = Role.of("Stellvertreter");
		var obmannRole = Role.of("Obmann");
		var financeRole = Role.of("Kassierer");
		var maintenantRole = Role.of("Hauptpächter");
		var subtenantRole = Role.of("Nebenpächter");
		var waterRole = Role.of("Wassermann");

		Tenant boss = new Tenant("Peter", "Klaus", "Am Berg 5, 12423 Irgendwo im Nirgendwo",
				"01242354356", "13.04.1999",
				userAccountManager.create("peter.klaus", password, "peter.klaus@email.com", maintenantRole));


		Tenant obmann = new Tenant("Hubert", "Grumpel", "Hinter den 7 Bergen, 98766 Zwergenhausen",
				"012345678", "04.09.1978",
				userAccountManager.create("hubertgrumpel", password, "hubert.grumpel2@cloud.com", maintenantRole));

		Tenant cashier = new Tenant("Bill", "Richart", "Am Bahnhof 25, 07875 Dorfdorf",
				"0123098874326", "13.05.1968", userAccountManager.create("bill", password, "billy,billbill@geld.com", subtenantRole));

		Tenant replacement = new Tenant("Sophie", "Kirmse", "Am Teichplatz 5, 67807 Meldetsichnie",
				"034567892132", "08.12.1988",
				userAccountManager.create("sophie", password, "s.krimse@gemaile.com", subtenantRole));

		Tenant protocol = new Tenant("Franziska", "Kiel", "Bei Isa",
				"0896548786890", "19.08.1998", userAccountManager.create("franziska", password, "francys@email.com", maintenantRole));

		Tenant waterman = new Tenant("Atlas", "Neptunius", "An der Promenade 34, 01298 Atlantis",
				"0980790789", "08.09.1567", userAccountManager.create("neptun", password, "neptuns.bart@fishmail.com", maintenantRole));

		obmann.addRole(obmannRole);
		boss.addRole(vorstandRole);
		cashier.addRole(financeRole);
		replacement.addRole(stellvertreterRole);
		protocol.addRole(protocolRole);
		waterman.addRole(waterRole);

		tenantRepository.saveAll(List.of(boss, obmann, cashier, replacement, protocol, waterman));
	}

	public void initializePlots(PlotCatalog plotCatalog, PlotService plotService) {
		if (this.plotService.existsByName("1")) {
			return;
		}

		LOG.info("Creating default plots");
		Plot Plot = plotService.addNewPlot("1", 300, "Kleine Parzelle mit angelegtem Teich.");
		LOG.info(Plot.getId().toString());
		Plot Plot_2 = plotService.addNewPlot("2", 500, "Sehr große Parzelle.");
		LOG.info(Plot_2.getId().toString());
		Plot Plot_3 = plotService.addNewPlot("3", 120, "Kleine Parzelle.");
		LOG.info(Plot_3.getId().toString());
		Plot Plot_4 = plotService.addNewPlot("4", 1500, "Sehr, sehr große Parzelle.");
		LOG.info(Plot_4.getId().toString());
		Plot Plot_5 = plotService.addNewPlot("5", 350, "Parzelle mit angelegtem Teich.");
		LOG.info(Plot_5.getId().toString());
		Plot Plot_6 = plotService.addNewPlot("6", 200, "Große Parzelle.");
		LOG.info(Plot_6.getId().toString());
		plotCatalog.saveAll(List.of(Plot, Plot_2, Plot_3, Plot_4, Plot_5, Plot_6));

		LOG.info("fast fertig");
	}

	public void initializeFee(FeeRepository feeRepository, FeeManager feeManager) {
		Assert.notNull(feeManager, "feeManager must not be null!");
		LOG.info("Creating default fee lists");
		if (feeManager.findAll().iterator().hasNext()) {
			return;
		}
		feeManager.save(new Fee("Wasserkosten", 1, 1.95, 0));
		feeManager.save(new Fee("Stromkosten", 1, 0.2, 0));
		feeManager.save(new Fee("Miete", 1, 0.18, 0));
		feeManager.save(new Fee("Strafgeld", 1, 8, 0));
		feeManager.save(new Fee("Mitgliedsbeitrag", 1, 17.25, 0));
		feeManager.save(new Fee("Haftpflichtbeitrag", 1, 0.35, 0));
		feeManager.save(new Fee("Winterdienst", 1, 3, 0));
		feeManager.save(new Fee("Sozialbeitrag", 1, 0.5, 0));
		feeManager.save(new Fee("Rechtsschutz", 1, 0.75, 0));
		feeManager.save(new Fee("Aufwandspauschale", 1, 12, 0));
		feeManager.save(new Fee("Sonstige Auslagen", 1, 1, 0));
		feeManager.save(new Fee("Grundmiete für Wasseruhr", 1, 2.6, 0));
		feeManager.save(new Fee("Grundmiete für Stromzähler", 1, 1.55, 0));

		LOG.info("Finished creating default fee lists");
	}

	public void initializeProcedures(ProcedureManager procedureManager) {
		if (!procedureManager.getAll().toList().isEmpty()) {
			return;
		}

		List<Plot> plots = plotCatalog.findAll().toList();
		List<Tenant> tenants = this.tenantRepository.findAll().toList();
		if (!plots.isEmpty() && !tenants.isEmpty()) {
			LOG.info("Creating default procedures");

			Tenant boss = null;
			Tenant obman = null;
			Tenant protocol = null;
			Tenant waterman = null;
			for (Tenant tenant :
					tenants) {
				if (tenant.getEmail().equals("peter.klaus@email.com")) {
					boss = tenant;
				} else if (tenant.getEmail().equals("hubert.grumpel2@cloud.com")) {
					obman = tenant;
				} else if (tenant.getEmail().equals("francys@email.com")) {
					protocol = tenant;
				} else if (tenant.getEmail().equals("neptuns.bart@fishmail.com")) {
					waterman = tenant;
				}
			}

			var procedure_1 = new Procedure(2019, plots.get(0), boss.getId());
			procedureManager.add(procedure_1);
			var procedure_2 = new Procedure(2019, plots.get(1), obman.getId());
			procedureManager.add(procedure_2);
			var procedure_3 = new Procedure(2019, plots.get(2), protocol.getId());
			procedureManager.add(procedure_3);
			var procedure_4 = new Procedure(2019, plots.get(3), waterman.getId());
			procedureManager.add(procedure_4);

			LOG.info("Finished creating default procedures");
		}
	}

}
