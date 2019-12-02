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

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class MessageServiceIntegrationTest {
	private final GreenMail greenMail = new GreenMail(new ServerSetup(3025, null, "smtp"));

	private MessageService messageService;

	public MessageServiceIntegrationTest(@Autowired MessageService messageService) {
		this.messageService = messageService;
	}

	@Test
	public void sendMessageTest() throws MessagingException {
		try {
			greenMail.start();

			final String to = "bar@example.com";
			//Use random content to avoid potential residual lingering problems
			final String subject = GreenMailUtil.random();
			final String text = GreenMailUtil.random();

			messageService.sendMessage(to, subject, text);

			// wait for max 5s for 1 email to arrive
			assertTrue(greenMail.waitForIncomingEmail(3000, 1));

			// Retrieve using GreenMail API
			MimeMessage[] messages = greenMail.getReceivedMessages();
			assertEquals(1, messages.length);

			MimeMessage msg = messages[0];
			assertEquals(subject, msg.getSubject());
			assertEquals(text, GreenMailUtil.getBody(msg).trim());
		} finally {
			greenMail.stop();
		}
	}
}
