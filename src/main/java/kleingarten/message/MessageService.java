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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MessageService {

	private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

	// Configuration for mail sending is defined in application.properties file.
	@Autowired
	private JavaMailSender mailSender;

	public void sendSimpleMessage(Message message) {
		SimpleMailMessage simpleMailMsg = new SimpleMailMessage();
		simpleMailMsg.setTo(message.getTo());
		simpleMailMsg.setSubject(message.getSubject());
		simpleMailMsg.setText(message.getText());

		try {
			mailSender.send(simpleMailMsg);
		} catch (MailException e) {
			LOG.error(e.getMessage());
		}
	}
}
