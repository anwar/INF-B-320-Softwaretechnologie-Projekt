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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Unit tests for {@link NewsEntry}.
 */
public class NewEntryUnitTests {

	/**
	 * Test that creation of {@link NewsEntry} with empty text is rejected.
	 */
	@Test
	void rejectsEmptyText() {
		assertThatExceptionOfType(IllegalArgumentException.class)//
				.isThrownBy(() -> new NewsEntry(""));
	}

	/**
	 * Test that creation of {@link NewsEntry} also creates the respective date.
	 */
	@Test
	void dateCreation() {
		assertThat(new NewsEntry("Lorem ipsum dolor sit amet, consectetur adipiscing elit.").getDate()).isNotNull();
	}
}
