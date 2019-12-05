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
import kleingarten.news.NewsEntry;
import kleingarten.news.NewsRepository;
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

	AppDataInitializer(NewsRepository news, WorkAssignmentRepository workAssignmentRepo, WorkAssignmentManager workAssignmentManager,
					   TenantRepository tenantRepository, UserAccountManager userAccountManager) {
		this.news = news;
		this.workAssignmentRepo = workAssignmentRepo;
		this.workAssignmentManager = workAssignmentManager;
		this.tenantRepository = tenantRepository;
		this.userAccountManager  = userAccountManager;
	}

	@Override
	public void initialize() {
		initializeNewsEntries(this.news);
		initializeWorkAssigment(this.workAssignmentRepo);
		initializeTenants(this.tenantRepository, this.userAccountManager);

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

		news.save(new NewsEntry("Deutsches Ipsum Dolor sit schnell consectetur Aperol Spritz elit, Die unendliche Geschichte do Fernweh tempor Grimms Märchen ut Handschuh et Bahnhof magna Frau Professor Ut Hallo ad Weihnachten veniam."));
		news.save(new NewsEntry("Guten Tag sint Bezirksschornsteinfegermeister cupidatat Wurst proident, Wiener Schnitzel in Joachim Löw qui Mertesacker deserunt Oktoberfest anim Wiener Schnitzel."));
		news.save(new NewsEntry("Jürgen Klinsmann aute Zeitgeist dolor Warmduscher reprehenderit Hörspiele voluptate Michael Schuhmacher esse Reinheitsgebot dolore Hockenheim fugiat Volkswagen pariatur."));
	}


	void initializeWorkAssigment(WorkAssignmentRepository workAssignmentRepo){
		Assert.notNull(workAssignmentRepo, "WorkAssignment must not be null");

		if (!this.workAssignmentManager.getAll().isEmpty()) {
			return;
		}
		LOG.info("create default Assignments");
		var Appointment = this.workAssignmentManager.createAssignmentForInitializer(LocalDateTime.of(2020,1,22,15,20,10), "Garten putzen", "Garten sauber machen Yalah");
		this.workAssignmentRepo.saveAll(List.of(Appointment));

	}

	void initializeTenants(TenantRepository tenantRepository, UserAccountManager userAccountManager){
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(tenantRepository, "TenantRepository must not be null");

		if(userAccountManager.findByUsername("peter.klaus").isPresent()){
			return;
		}

		LOG.info("Creating default users and customers");
		LOG.info("Habe fertig");

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
			userAccountManager.create("peter.klaus", password,"peter.klaus@email.com", maintenantRole));


		Tenant obmann = new Tenant("Hubert", "Grumpel", "Hinter den 7 Bergen, 98766 Zwergenhausen",
			"012345678", "04.09.1978",
			userAccountManager.create("hubertgrumpel", password,"hubert.grumpel2@cloud.com", maintenantRole));

		Tenant cashier = new Tenant("Bill", "Richart", "Am Bahnhof 25, 07875 Dorfdorf",
			"0123098874326", "13.05.1968", userAccountManager.create("bill", password,"billy,billbill@geld.com", subtenantRole));

		Tenant replacement = new Tenant("Sophie", "Kirmse", "Am Teichplatz 5, 67807 Meldetsichnie",
			"034567892132", "08.12.1988",
			userAccountManager.create("sophie", password, "s.krimse@gemaile.com",subtenantRole));

		Tenant protocol = new Tenant("Franziska", "Kiel", "Bei Isa",
			"0896548786890", "19.08.1998", userAccountManager.create("franziska", password, "francys@email.com",maintenantRole));

		Tenant waterman = new Tenant("Atlas", "Neptunius", "An der Promenade 34, 01298 Atlantis",
			"0980790789","08.09.1567", userAccountManager.create("neptun", password,"neptuns.bart@fishmail.com", maintenantRole));

		obmann.addRole(obmannRole);
		boss.addRole(vorstandRole);
		cashier.addRole(financeRole);
		replacement.addRole(stellvertreterRole);
		protocol.addRole(protocolRole);
		waterman.addRole(waterRole);

		tenantRepository.saveAll(List.of(boss, obmann, cashier, replacement, protocol, waterman));
	}

}
