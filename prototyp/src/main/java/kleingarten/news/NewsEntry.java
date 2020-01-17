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

import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

// A news entry. An entity as in the Domain Driven Design context. Mapped onto the database using JPA annotations.
@Entity
class NewsEntry {

	private @Id @GeneratedValue Long id;
	private final String text;
	private final LocalDateTime date;

	/**
	 * Creates a new {@link NewsEntry} for the given text.
	 *
	 * @param text must not be {@literal null} or empty
	 */
	public NewsEntry(String text) {
		Assert.hasText(text, "Text must not be null or empty!");

		this.text = text;
		this.date = LocalDateTime.now();
	}

	@SuppressWarnings("unused")
	private NewsEntry() {
		this.text = null;
		this.date = null;
	}


	public Long getId() {

		return id;
	}

	public LocalDateTime getDate() {

		return date;
	}

	public String getText() {

		return text;
	}
}
