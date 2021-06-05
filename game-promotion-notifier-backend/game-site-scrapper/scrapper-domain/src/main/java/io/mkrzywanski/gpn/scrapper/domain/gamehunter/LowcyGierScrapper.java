package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class LowcyGierScrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LowcyGierScrapperService.class);

    private final LowcyGierClient lowcyGierClient;
    private final LowcyGierParser lowcyGierParser;

    LowcyGierScrapper(final LowcyGierClient lowcyGierClient,
                      final LowcyGierParser lowcyGierParser) {
        this.lowcyGierClient = lowcyGierClient;
        this.lowcyGierParser = lowcyGierParser;
    }

    List<Post> scrap(final int pageNumber) {
        try {
            final String pageHtml = lowcyGierClient.getPage(pageNumber);
            return lowcyGierParser.parse(pageHtml);
        } catch (final LowcyGierClientException e) {
            LOGGER.info("Error while connection to site. {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
