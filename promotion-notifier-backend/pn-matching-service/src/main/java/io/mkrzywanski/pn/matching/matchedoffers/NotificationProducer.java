package io.mkrzywanski.pn.matching.matchedoffers;

import io.mkrzywanski.pn.matching.user.UserSerivceClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class NotificationProducer {

    private final MatchesRepository matchesRepository;
    private final UserSerivceClient userServiceClient;
    private final NotificationPublisher notificationPublisher;

    NotificationProducer(final MatchesRepository matchesRepository,
                                final UserSerivceClient userServiceClient,
                                final NotificationPublisher notificationPublisher) {
        this.matchesRepository = matchesRepository;
        this.userServiceClient = userServiceClient;
        this.notificationPublisher = notificationPublisher;
    }

    //at most once semantics since publishing from transaction
    @Scheduled(cron = "${gpn.matching-service.publishing.cron}")
    @Transactional
    public void publish() {
        final var allUsersMatches = matchesRepository.findAll();
        allUsersMatches.stream()
                .filter(UserOfferMatches::hasPosts)
                .map(toNotifications())
                .forEach(notificationPublisher::publish);
        matchesRepository.removeAlreadyNotifiedPosts(allUsersMatches);
    }

    private Function<UserOfferMatches, NewOffersNotification> toNotifications() {
        return userOfferMatches -> {
            final var userDetails = userServiceClient.getUserDetails(userOfferMatches.getUserId());
            final var postNotifications = userOfferMatches.getPostEntities().stream().map(postEntity -> new PostNotificationData(postEntity.getLink(), postEntity.getOfferEntities().stream().map(offerEntity -> new OfferNotificationData(offerEntity.getName(), offerEntity.getLink(), offerEntity.getGamePrice())).collect(Collectors.toList()))).collect(Collectors.toList());
            return new NewOffersNotification(userDetails, postNotifications);
        };
    }
}
