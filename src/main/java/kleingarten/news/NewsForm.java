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

import javax.validation.constraints.NotBlank;

// Type to bind request payloads and make them available in the controller.
class NewsForm {
	private final @NotBlank String text;

	/**
	 * Creates a new {@link NewsForm} with the given name and text. Spring Framework will use this constructor to
	 * bind the values provided in the web form described in {@code src/main/resources/templates/home.html}, in
	 * particular the {@code text} fields as they correspond to the parameter names of the constructor.
	 * The constructor needs to be public so that Spring will actually consider it for form data binding.
	 *
	 * @param text the value to bind to {@code text}
	 */
	public NewsForm(String text) {

		this.text = text;
	}

	/**
	 * Returns the value bound to the {@code text} attribute of the request. Needs to be public so that Spring will
	 * actually consider it for form data binding.
	 *
	 * @return the value bound to {@code text}
	 */
	public String getText() {
		return text;
	}

	/**
	 * Returns a new {@link NewsEntry} using the data submitted in the request.
	 *
	 * @return the newly created {@link NewsEntry}
	 * @throws IllegalArgumentException if you call this on an instance without the text actually set.
	 */
	NewsEntry toNewsEntry() {
		return new NewsEntry(getText());
	}
}
