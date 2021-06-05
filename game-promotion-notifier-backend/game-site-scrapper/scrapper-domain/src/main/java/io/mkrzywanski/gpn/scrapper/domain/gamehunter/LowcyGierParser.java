package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer;
import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class LowcyGierParser {

    private static final Evaluator.Tag ARTICLE = new Evaluator.Tag("article");

    List<Post> parse(final String html) {
        final Document document = Jsoup.parse(html);
        final Elements articlesContents = document.select(ARTICLE);

        return articlesContents.stream().map(LowcyGierParser::extractPostInfo).collect(Collectors.toList());
    }

    private static Post extractPostInfo(final Element element) {
        final Elements offersForArticle = element.children()
                .select("div.text-wrapper.lead-wrapper")
                .select("ul > li > strong");
        final ZonedDateTime postDateTime = extractPostTime(element);
        final List<GameOffer> gameOffers = offersForArticle.stream()
                .map(LowcyGierParser::extractGameOffer)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return new Post(Hash.compute(element.text()), "lowcy-gier", gameOffers, postDateTime);
    }

    private static ZonedDateTime extractPostTime(final Element element) {
        final Element timeTag = element.children().select("time").first();
        final String datetime = timeTag.attr("datetime");
        return ZonedDateTime.parse(datetime);
    }

    private static Optional<GameOffer> extractGameOffer(final Element element) {
        final String price = element.ownText();
        final Elements aTags = element.children().select("a");
        if (aTags.isEmpty()) {
            return Optional.empty();
        } else {
            final Element aTag = aTags.first();
            final String gameName = aTag.ownText();
            final String link = aTag.attr("href");
            return Optional.of(new GameOffer(gameName, LowcyGierPriceParser.parse(price), link));
        }
    }
}
