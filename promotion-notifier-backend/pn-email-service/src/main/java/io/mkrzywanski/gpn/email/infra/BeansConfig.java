package io.mkrzywanski.gpn.email.infra;

import io.mkrzywanski.gpn.email.domain.EmailAddress;
import io.mkrzywanski.gpn.email.domain.EmailSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class BeansConfig {

    @Autowired
    MailProperties mailProperties;

    @Bean
    EmailSendingService emailSendingService(final JavaMailSender javaMailSender) {
        return new EmailSendingService(javaMailSender, EmailAddress.of(mailProperties.getUsername()));
    }
}

