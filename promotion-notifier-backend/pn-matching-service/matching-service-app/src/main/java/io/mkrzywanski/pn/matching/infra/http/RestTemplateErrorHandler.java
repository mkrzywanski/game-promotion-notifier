package io.mkrzywanski.pn.matching.infra.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mkrzywanski.pn.webservice.common.error.ErrorResponse;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

    private final ObjectMapper objectMapper;

    public RestTemplateErrorHandler(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleError(final ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                final String httpBodyResponse = reader.lines().collect(Collectors.joining(""));
                final ErrorResponse errorResponse = objectMapper.readValue(httpBodyResponse, ErrorResponse.class);
                throw ClientCommunicationException.builder()
                        .errorResponse(errorResponse)
                        .build();
            }
        }
    }
}
