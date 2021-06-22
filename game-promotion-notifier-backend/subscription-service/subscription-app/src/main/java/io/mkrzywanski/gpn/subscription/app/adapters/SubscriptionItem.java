package io.mkrzywanski.gpn.subscription.app.adapters;

public class SubscriptionItem {

    private final String value;

    public SubscriptionItem(final String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}
