package io.mkrzywanski.pn.user.app;

import io.opentelemetry.instrumentation.spring.autoconfigure.EnableOpenTelemetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableOpenTelemetry
public class UserServiceApplication {
    public static void main(final String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
