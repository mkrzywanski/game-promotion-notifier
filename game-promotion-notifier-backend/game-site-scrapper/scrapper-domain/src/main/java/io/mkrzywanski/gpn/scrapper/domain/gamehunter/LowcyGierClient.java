package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class LowcyGierClient {

    private static final String PAGE_REQUEST_FORMAT = "%s/page/%s/";
    private final String baseUrl;
    private final HttpClient httpClient;

    LowcyGierClient(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
    }

    public String getPage(final int pageNumber) throws LowcyGierClientException {
        final URI uri = URI.create(PAGE_REQUEST_FORMAT.formatted(baseUrl, pageNumber));
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new LowcyGierClientException();
        }
    }
}
