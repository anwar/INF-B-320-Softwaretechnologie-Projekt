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
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link NewsController}.
 */
@SpringBootTest
public class NewsControllerUnitTests {
	@Mock
	NewsRepository news;

	/**
	 * Test population of Spring {@link Model}.
	 */
	@Test
	void modelPopulation() {
		NewsEntry entry = new NewsEntry("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
		doReturn(Streamable.of(entry)).when(news).findAll();

		Model model = new ExtendedModelMap();

		NewsController controller = new NewsController(news);
		String viewName = controller.home(model, new NewsForm(null));

		assertThat(viewName).isEqualTo("news/home");
		assertThat(model.asMap().get("newsEntries")).isInstanceOf(Iterable.class);
		assertThat(model.asMap().get("newsForm")).isNotNull();

		verify(news, times(1)).findAll();
	}
}
