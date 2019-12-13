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

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/**
 * A controller to handle web requests to manage {@link NewsEntry}s.
 */
@Controller
class NewsController {
	private final NewsRepository news;

	/**
	 * @param news must not be {@literal null}
	 */
	public NewsController(NewsRepository news) {
		Assert.notNull(news, "News must not be null!");
		this.news = news;
	}

	/**
	 * Handles requests to the application root URI.
	 * Note, that you can use {@code redirect:} as prefix to trigger a browser
	 * redirect instead of simply rendering a view.
	 *
	 * @return a redirect string
	 */
	@GetMapping(path = "/")
	public String index() {
		return "redirect:/home";
	}

	/**
	 * Handles requests to access the home page. Obtains all currently available {@link NewsEntry}s and puts them
	 * into the {@link Model} that's used to render the view.
	 *
	 * @param model that's used to render the view
	 * @return a view name
	 */
	@GetMapping(path = "/home")
	String home(Model model) {
		model.addAttribute("newsEntries", news.findAll());

		return "news/home";
	}

	/**
	 * Handles requests to create a new {@link NewsEntry}. Spring MVC automatically validates and binds the
	 * HTML form to the {@code form} parameter. Validation or binding errors, if any, are exposed via
	 * the {@code errors} parameter.
	 * This request can only be performed by authenticated users with "Vorstandsvorsitzender" role.
	 *
	 * @param text for {@link NewsEntry}
	 * @return a redirect string
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@PostMapping(path = "/home/news/addEntry")
	String addEntry(@RequestParam("text") String text) {
		NewsEntry entry = new NewsEntry(text);
		news.save(entry);

		return "redirect:/home";
	}

	/**
	 * Deletes a {@link NewsEntry}.
	 * This request can only be performed by authenticated users with "Vorstandsvorsitzender" role.
	 *
	 * @param id of the {@link NewsEntry} to delete
	 * @return a redirect string
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@PostMapping(path = "/home/news/deleteEntry/{id}")
	String deleteEntry(@PathVariable("id") long id) {
		NewsEntry entry = news.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		news.delete(entry);

		return "redirect:/home";
	}

	/**
	 * Updates a {@link NewsEntry}.
	 * This request can only be performed by authenticated users with "Vorstandsvorsitzender" role.
	 *
	 * @param id   of the {@link NewsEntry} to delete
	 * @param text for the updated entry
	 * @return a redirect string
	 */
	@PreAuthorize("hasRole('Vorstandsvorsitzender')")
	@PostMapping(path = "/home/news/editEntry/{id}")
	String editEntry(@PathVariable("id") long id, @RequestParam("text") String text) {
		NewsEntry entry = news.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		entry.setText(text);
		entry.setDate(LocalDateTime.now());
		news.save(entry);

		return "redirect:/home";
	}
}
