package io.mkrzywanski.gpn.scrapper.domain.steam;

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer;
import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.NumberGamePrice;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostId;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import io.mkrzywanski.gpn.scrapper.domain.post.PostTransactionalOutboxRepository;
import io.mkrzywanski.gpn.scrapper.domain.post.price.GamePrice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SteamScrapperService {
    private static final String GAME_LINK_FORMAT = "https://store.steampowered.com/agecheck/app/%s/";
    private static final ZonedDateTime UTC = ZonedDateTime.now(ZoneId.of("UTC"));
    private final SteamApiClient steamApiClient;
    private final PostRepository postRepository;
    private final PostTransactionalOutboxRepository postTransactionalOutboxRepository;

    public SteamScrapperService(final SteamApiClient steamApiClient, final PostRepository postRepository, final PostTransactionalOutboxRepository postTransactionalOutboxRepository) {
        this.steamApiClient = steamApiClient;
        this.postRepository = postRepository;
        this.postTransactionalOutboxRepository = postTransactionalOutboxRepository;
    }

    public void scrap() {
        final List<SteamApp> strings = steamApiClient.allGameIds();
        final var gameToNameMap = strings.stream().distinct().collect(Collectors.toMap(SteamApp::appid, SteamApp::gameName));
        final var appIds = strings.stream().map(SteamApp::appid).collect(Collectors.toList());
        final var stringGameInfoMap = steamApiClient.getGameOffersFor(appIds);

        final List<Post> steam = stringGameInfoMap.entrySet().stream()
                .map(entry -> toFullGameInfo(entry, gameToNameMap))
                .filter(FullGameInfo::isSuccess)
                .filter(FullGameInfo::isOnSale)
                .map(this::toPost)
                .toList();


        final Set<Hash> allPostHashes = steam.stream()
                .map(Post::getHash)
                .collect(Collectors.toSet());

        final List<Hash> alreadySavedPostHashes = postRepository.findByHashIn(allPostHashes);
        final Set<Hash> newPostHashes = new HashSet<>(allPostHashes);
        alreadySavedPostHashes.forEach(newPostHashes::remove);

        final Set<Post> newPosts = steam.stream()
                .filter(post -> newPostHashes.contains(post.getHash()))
                .collect(Collectors.toSet());

        postRepository.saveAll(newPosts);
        postTransactionalOutboxRepository.put(newPosts);

    }

    private FullGameInfo toFullGameInfo(final Map.Entry<Integer, GameInfo> e, final Map<Integer, String> gameToNameMap) {
        final Integer gameId = e.getKey();
        return new FullGameInfo(gameId, gameToNameMap.get(gameId), e.getValue());
    }

    private Post toPost(final FullGameInfo fullGameInfo) {
        final String gameName = fullGameInfo.gameName();
        final GamePrice gamePrice = toGamePrice(fullGameInfo.gameInfo().data().priceInfo());

        final GameOffer gameOffer = new GameOffer(UUID.randomUUID(), gameName, gamePrice, GAME_LINK_FORMAT.formatted(fullGameInfo.gameId()));
        return new Post(PostId.generate(), Hash.compute(fullGameInfo.toString()), "Steam", List.of(gameOffer), UTC);

    }

    GamePrice toGamePrice(final PriceOverview priceOverview) {
        final BigDecimal price = BigDecimal.valueOf(priceOverview.finalPrice())
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN);
        final Currency currency = Currency.getInstance(priceOverview.currency());
        return new NumberGamePrice(currency, price);

    }
}
