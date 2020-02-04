// Copyright 2019-2020 the original author or authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package kleingarten.news;

import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

// A news entry is an entity in the Domain Driven Design context. Mapped onto the database using JPA annotations.
@Entity
public class NewsEntry {
	@Id
	@GeneratedValue
	private Long id;
	private String text;
	private LocalDateTime date;

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

	/**
	 * Getter for the id of a {@link NewsEntry}
	 *
	 * @return id of type {@link Long }
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Getter for the date of a {@link NewsEntry}
	 *
	 * @return date of type {@link LocalDateTime }
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Setter for the date of a {@link NewsEntry}
	 *
	 * @param date of type {@link LocalDateTime }
	 */
	public void setDate(LocalDateTime date) {
		Assert.notNull(date, "date must not be null!");
		this.date = date;
	}

	/**
	 * Getter for the text of a {@link NewsEntry}
	 *
	 * @return text of type {@link String }
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter for the date of a {@link NewsEntry}
	 *
	 * @param text of type {@link String}
	 */
	public void setText(String text) {
		Assert.hasText(text, "Text must not be null or empty!");
		this.text = text;
	}
}
