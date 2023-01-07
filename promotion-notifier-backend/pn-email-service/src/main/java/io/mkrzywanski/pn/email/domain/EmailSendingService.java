package io.mkrzywanski.pn.email.domain;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

public class EmailSendingService {

    private final JavaMailSender javaMailSender;
    private final EmailAddress sender;

    public EmailSendingService(final JavaMailSender javaMailSender, final EmailAddress sender) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }

    public void send(final EmailMessage emailMessage) throws EmailDeliveryFailedException {
        try {
            doSend(emailMessage);
        } catch (final MessagingException | MailException e) {
            throw new EmailDeliveryFailedException(e);
        }
    }

    private void doSend(final EmailMessage emailMessage) throws MessagingException {
        final MimeMessage message = javaMailSender.createMimeMessage();
        populateMimeMessage(emailMessage, message);
        javaMailSender.send(message);
    }

    private void populateMimeMessage(final EmailMessage emailMessage, final MimeMessage message) throws MessagingException {
        final MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_NO,
                StandardCharsets.UTF_8.name());
        helper.setTo(emailMessage.toString());
        helper.setText(emailMessage.emailContent(), true);
        helper.setSubject(emailMessage.subject());
        helper.setFrom(sender.getEmail());
    }
}
