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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Integration tests for {@link NewsController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerIntegrationTests {
	@Autowired
	MockMvc mvc;

	@Autowired
	NewsRepository news;

	/**
	 * Test that root URI is redirected to "/home".
	 */
	@Test
	void redirectRootUriToHome() throws Exception {
		mvc.perform(get("/")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(view().name("redirect:/home"));
	}

	/**
	 * Test that a {@link NewsEntry} is successfully added and user is
	 * redirected to "/home".
	 */
	@Test
	@WithMockUser(roles = "Vorstandsvorsitzender")
	void addEntryAndRedirectToHome() throws Exception {
		long noOfEntries = news.count();
		String text = "This is the new text for the entry";

		mvc.perform(post("/home/news/addEntry")
				.param("text", text))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/home"));

		assertThat(news.count()).isEqualTo(noOfEntries + 1);
	}

	/**
	 * Test that a {@link NewsEntry} is successfully deleted and user is
	 * redirected to "/home".
	 */
	@Test
	@WithMockUser(roles = "Vorstandsvorsitzender")
	void deleteEntryAndRedirectToHome() throws Exception {
		long noOfEntries = news.count();
		NewsEntry entry = news.findAll().iterator().next();

		mvc.perform(post("/home/news/deleteEntry/{id}", entry.getId()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/home"));

		assertThat(news.count()).isEqualTo(noOfEntries - 1);
	}

	/**
	 * Test that a {@link NewsEntry} is successfully updated and user is
	 * redirected to "/home".
	 */
	@Test
	@WithMockUser(roles = "Vorstandsvorsitzender")
	void editEntryAndRedirectToHome() throws Exception {
		long noOfEntries = news.count();
		NewsEntry entry = news.findAll().iterator().next();
		long id = entry.getId();

		String text = "This is the new text for the entry";

		mvc.perform(post("/home/news/editEntry/{id}", id)
				.param("text", text))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/home"));

		assertThat(news.count()).isEqualTo(noOfEntries);

		NewsEntry updatedEntry = news.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		assertThat(updatedEntry.getText()).isEqualTo(text);
	}
}
