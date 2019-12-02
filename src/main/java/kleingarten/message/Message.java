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
package kleingarten.message;

public class Message {

	private final String to;
	private final String subject;
	private final String text;

	public Message(String to, String subject, String text) {
		this.to = to;
		this.subject = subject;
		this.text = text;
	}

	public String getTo() {
		return to;
	}

	public String getSubject() {
		return subject;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return String.format("Message{to=%s, subject=%s, text=%s}", to, subject, text);
	}
}
