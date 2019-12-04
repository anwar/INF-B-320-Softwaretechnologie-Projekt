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

import kleingarten.appointment.WorkAssignment;
import kleingarten.appointment.WorkAssignmentManager;
import kleingarten.appointment.WorkAssignmentRepository;
import kleingarten.news.NewsEntry;
import kleingarten.news.NewsRepository;
import org.salespointframework.core.DataInitializer;
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

	AppDataInitializer(NewsRepository news, WorkAssignmentRepository workAssignmentRepo, WorkAssignmentManager workAssignmentManager) {
		this.news = news;
		this.workAssignmentRepo = workAssignmentRepo;
		this.workAssignmentManager = workAssignmentManager;
	}

	@Override
	public void initialize() {
		initializeNewsEntries(this.news);
		initializeWorkAssigment(this.workAssignmentRepo);
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

}
