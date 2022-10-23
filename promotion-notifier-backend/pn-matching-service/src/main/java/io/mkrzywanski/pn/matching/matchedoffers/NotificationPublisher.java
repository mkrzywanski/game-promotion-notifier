package io.mkrzywanski.pn.matching.matchedoffers;

interface NotificationPublisher {
    void publish(NewOffersNotification newOffersNotification);
}
