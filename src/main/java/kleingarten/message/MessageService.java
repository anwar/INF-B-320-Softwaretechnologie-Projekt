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

package kleingarten.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * {@link MessageService} provides convenient methods based on the {@link org.springframework.mail} package
 * to send emails using a SMTP server.
 * The methods are based on the official Spring documentation and the guide on Baeldung website.
 * Links for reference:
 * https://docs.spring.io/spring/docs/5.2.2.RELEASE/spring-framework-reference/integration.html#mail}
 * https://www.baeldung.com/spring-email
 */
@Service
public class MessageService {
	private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
	private MessageProperties messageProperties;
	private JavaMailSender mailSender;

	private boolean enabled;
	private boolean printToLog;

	/**
	 * Spring Framework provides an easy abstraction for sending email by using the {@link JavaMailSender}
	 * interface. Spring creates a {@link JavaMailSender}, if none exists, using the configuration provided
	 * in {@code src/main/resources/application.properties}.
	 *
	 * @param mailSender        is the {@link JavaMailSender} used for sending emails
	 * @param messageProperties are the custom defined application properties
	 */
	public MessageService(@Autowired MessageProperties messageProperties, @Autowired JavaMailSender mailSender) {
		this.messageProperties = messageProperties;
		this.mailSender = mailSender;

		this.enabled = this.messageProperties.isEnabled();
		this.printToLog = this.messageProperties.isLogging();
	}

	/**
	 * A method for sending a {@link SimpleMailMessage}.
	 *
	 * @param to      email address of the recipient
	 * @param subject for the email
	 * @param text    for the email
	 */
	public void sendMessage(String to, String subject, String text) {
		if (printToLog) {
			LOG.info(String.format(
					"Sending an email to \"%s\" with subject \"%s\" and message \"%s\"",
					to, subject, text));
		}

		if (!enabled) {
			return; // no need to proceed further
		}

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
	 * @param to                   email address of the recipient
	 * @param subject              for the email
	 * @param text                 for the email
	 * @param attachmentFilename   for the email
	 * @param attachmentDataSource for the email
	 */
	public void sendMessageWithAttachment(String to,
										  String subject,
										  String text,
										  String attachmentFilename,
										  DataSource attachmentDataSource) {
		if (printToLog) {
			LOG.info(String.format(
					"Sending an email to \"%s\" with subject \"%s\"" +
							" and message \"%s\" and an attachment with name \"%s\"",
					to, subject, text, attachmentFilename));
		}

		if (!enabled) {
			return; // no need to proceed further
		}

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);
			helper.addAttachment(attachmentFilename, attachmentDataSource);

			mailSender.send(message);
		} catch (MessagingException | MailException e) {
			LOG.error(e.getMessage());
		}
	}
}
