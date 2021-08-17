package io.mkrzywanski.pn.user.app.infra;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {

        final JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addSerializer(Instant.class, new InstantSerializer())
        return Jackson2ObjectMapperBuilder.json()
                .modules(javaTimeModule)
//                .dateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX"))
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
