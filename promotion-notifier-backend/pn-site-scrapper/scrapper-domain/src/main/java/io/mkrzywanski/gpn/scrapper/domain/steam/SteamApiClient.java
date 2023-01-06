package io.mkrzywanski.gpn.scrapper.domain.steam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class SteamApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(SteamApiClient.class);
    private static final int BATCH_SIZE = 50;
    private static final int OK = 200;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;


    SteamApiClient() {
        httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.objectMapper = configureObjectMapper();
    }

    private ObjectMapper configureObjectMapper() {
        final SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(GameInfo.class, new JsonMappingErrorHandlingDeserializer<>(new GameInfoDeserializer()));
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    List<SteamApp> allGameIds() {
        final String urlFormat = "https://api.steampowered.com/ISteamApps/GetAppList/v2";
        final URI uri = URI.create(urlFormat);
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            return doSend(request);
        } catch (IOException | InterruptedException e) {
            LOG.warn(e.getMessage());
            return List.of();
        }
    }

    private List<SteamApp> doSend(final HttpRequest request) throws IOException, InterruptedException {
        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != OK) {
            LOG.warn("Error while calling steam api to get game ids. Status code : {}", response.statusCode());
            return List.of();
        }

        final String appsJson = JsonPath.parse(response.body())
                .read("$.applist.apps").toString();
        return objectMapper.readValue(appsJson, new TypeReference<>() {
        });
    }

    Map<Integer, GameInfo> getGameOffersFor(final List<Integer> appIds) {

        final List<List<Integer>> partition = Lists.partition(appIds, BATCH_SIZE);

        return partition.stream()
                .map(this::call)
                .filter(Objects::nonNull)
                .collect(HashMap::new, Map::putAll, Map::putAll);

    }

    private Map<Integer, GameInfo> call(final List<Integer> appIds) {
        final String appIdsQueryParam = String.join(",", appIds.stream().map(String::valueOf).toList());
        final String baseUrl = "https://store.steampowered.com/api/appdetails?appids=%s&cc=en&filters=price_overview";
        final String formattedUrl = baseUrl.formatted(appIdsQueryParam);
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(formattedUrl))
                .build();

        try {
            final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage());
            return Map.of();
        }
    }

}
