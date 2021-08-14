package io.mkrzywanski.pn.matching.infa.http;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(final ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                final String httpBodyResponse = reader.lines().collect(Collectors.joining(""));
                throw ClientCommunicationException.builder()
                        .httpStatus(response.getStatusCode())
                        .serviceName("user-service")
                        .message(httpBodyResponse)
                        .build();
            }
        }
    }
}
