package io.mkrzywanski.pn.subscription;

public class SubscriptionEntry {
    private final String value;

    public SubscriptionEntry(final String value) {
        this.value = value;
    }

    public String asString() {
        return value;
    }
}
