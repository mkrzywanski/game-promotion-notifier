package io.mkrzywanski.gpn.subscription;

import java.util.List;
import java.util.UUID;

public class Post {
    private UUID postId;
    private List<Offer> offers;

    private Post() {
    }

    public Post(final UUID postId, final List<Offer> offers) {
        this.postId = postId;
        this.offers = offers;
    }

    public UUID getPostId() {
        return postId;
    }

    public List<Offer> getOffers() {
        return offers;
    }
}
