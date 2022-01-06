package io.mkrzywanski.pn.matching.matchedoffers;

public interface NotificationPublisher {
    void publish(NewOffersNotification newOffersNotification);
}
