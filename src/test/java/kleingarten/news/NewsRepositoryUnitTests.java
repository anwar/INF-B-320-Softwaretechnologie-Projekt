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
package kleingarten.news;

import kleingarten.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Tests for {@link NewsRepository}.
 * Bootstraps the application using the {@link Application} configuration class.
 * Enables transaction rollbacks after test methods using the {@link Transactional} annotation.
 */
@SpringBootTest
@Transactional
public class NewsRepositoryUnitTests {
	@Autowired
	NewsRepository news;

	/**
	 * Test the persistence of {@link NewsRepository}.
	 */
	@Test
	void newsEntryPersistence() {
		NewsEntry entry = news.save(new NewsEntry("Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
		assertThat(news.findAll()).contains(entry);
	}
}
