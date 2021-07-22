package io.mkrzywanski.gpn.email.config

import io.mkrzywanski.gpn.email.smtp.SmtpServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
@EnableConfigurationProperties(MailProperties)
class SmtpServerConfig {

    @Autowired
    MailProperties mailProperties

    @Bean
    def smtpServer() {
        def smtpServer = SmtpServer.builder()
                .email(mailProperties.username)
                .userName(mailProperties.username)
                .password(mailProperties.password)
                .build()
        smtpServer.start()
        smtpServer
    }

    @Bean
    //this has to be defined as JavaMailSender - cannot use def - otherwise tests fail - to investigate why
    JavaMailSender javaMailSender() {
        JavaMailSender sender = new JavaMailSenderImpl()
        sender.setHost(mailProperties.host)
        sender.setPort(smtpServer().port())
        sender.setUsername(mailProperties.username)
        sender.setPassword(mailProperties.password)
        sender.setProtocol(mailProperties.protocol)
        sender.setDefaultEncoding(mailProperties.defaultEncoding.name())
        if (!properties.getProperties().isEmpty()) {
            sender.setJavaMailProperties(asProperties(properties.getProperties()))
        }
        sender
    }

    private static Properties asProperties(final Map<String, String> source) {
        Properties properties = new Properties()
        properties.putAll(source)
        return properties
    }

}
