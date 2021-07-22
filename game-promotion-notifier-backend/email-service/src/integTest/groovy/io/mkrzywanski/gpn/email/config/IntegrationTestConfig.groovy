package io.mkrzywanski.gpn.email.config

import io.mkrzywanski.gpn.email.EmailServiceApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import([RabbitMQIntegConfig, SmtpServerConfig, EmailServiceApplication])
@EnableAutoConfiguration
class IntegrationTestConfig {
}
