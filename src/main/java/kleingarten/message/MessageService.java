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

/**
 * {@link MessageService} provides convenient methods based on the {@link org.springframework.mail} package
 * to send emails using a SMTP server.
 * The methods are based on the official Spring documentation and the guide on Baeldung website.
 *
 * @see https://docs.spring.io/spring/docs/5.2.2.RELEASE/spring-framework-reference/integration.html#mail
 * @see https://www.baeldung.com/spring-email
 */
@Service
public class MessageService {
	private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

	private JavaMailSender mailSender;

	/**
	 * Spring Framework provides an easy abstraction for sending email by using the {@link JavaMailSender}
	 * interface. Spring creates a {@link JavaMailSender}, if none exists, using the configuration provided
	 * in {@code src/main/resources/application.properties}.
	 */
	public MessageService(@Autowired JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * A method for sending a {@link SimpleMailMessage}.
	 *
	 * @param to      email address of the recipient
	 * @param subject for the email
	 * @param text    for the email
	 */
	public void sendMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (MailException e) {
			LOG.error(e.getMessage());
		}
	}

	/**
	 * A method for sending a multi part {@link MimeMessage}.
	 *
	 * @param to               email address of the recipient
	 * @param subject          for the email
	 * @param text             for the email
	 * @param pathToAttachment for the email
	 */
	public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);

			FileSystemResource file = new FileSystemResource(new File(pathToAttachment));

			String fileName = file.getFilename();
			if (fileName == null || fileName.isEmpty()) {
				fileName = "attachment";
			}

			helper.addAttachment(fileName, file);

			mailSender.send(message);
		} catch (MessagingException | MailException e) {
			LOG.error(e.getMessage());
		}
	}
}
