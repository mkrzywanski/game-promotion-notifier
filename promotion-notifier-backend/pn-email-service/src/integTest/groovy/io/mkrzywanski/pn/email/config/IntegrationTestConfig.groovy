package io.mkrzywanski.pn.email.config


import io.mkrzywanski.pn.email.EmailServiceApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import([RabbitMQIntegConfig, SmtpServerConfig, EmailServiceApplication])
@EnableAutoConfiguration
class IntegrationTestConfig {
}
