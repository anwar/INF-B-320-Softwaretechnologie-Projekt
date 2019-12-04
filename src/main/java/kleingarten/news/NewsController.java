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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * A controller to handle web requests to manage {@link NewsEntry}s
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

	@RequestMapping("/")
	public String index() {

		return "redirect:/home";
	}

	/**
	 * Handles requests to access the home. Obtains all currently available {@link NewsEntry}s and puts them
	 * into the {@link Model} that's used to render the view.
	 *
	 * @param model the model that's used to render the view
	 * @param form  the form to be added to the model
	 * @return a view name
	 */
	@GetMapping(path = "/home")
	String home(Model model, @ModelAttribute(binding = false) NewsForm form) {

		model.addAttribute("entries", news.findAll());
		model.addAttribute("form", form);

		return "news/home";
	}

	/**
	 * Handles requests to create a new {@link NewsEntry}. Spring MVC automatically validates and binds the
	 * HTML form to the {@code form} parameter. Validation or binding errors, if any, are exposed via the {@code
	 * errors} parameter.
	 *
	 * @param model  the model that's used to render the view
	 * @param errors an object that stores any form validation or data binding errors
	 * @param form   the form submitted by the user
	 * @return a redirect string
	 */
	@PostMapping(path = "/home")
	String addEntry(@Valid @ModelAttribute("form") NewsForm form, Errors errors, Model model) {

		if (errors.hasErrors()) {
			return home(model, form);
		}

		news.save(form.toNewEntry());

		return "redirect:/home";
	}

	// TODO (Talal): Add mappings for editing and removing existing news entries.
}
