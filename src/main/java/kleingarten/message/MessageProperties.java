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

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * {@link MessageProperties} provides Spring configuration properties to change the behavior of the
 * message package.
 * The configuration properties can be configured in {@code src/main/resources/application.properties}.
 */
@ConfigurationProperties("app.messaging")
class MessageProperties {
	// The boolean property controls whether the contents of the emails should be
	// logged to console.
	private boolean logging = true;

	// This boolean property controls whether the emails should be sent using the
	// configured mail server credentials.
	// This is useful when we only want to debug and not send out any actual emails.
	private boolean enabled = false;

	public boolean isLogging() {
		return logging;
	}

	public void setLogging(boolean logging) {
		this.logging = logging;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
