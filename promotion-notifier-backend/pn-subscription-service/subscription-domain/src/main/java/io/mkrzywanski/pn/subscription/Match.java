package io.mkrzywanski.pn.subscription;

import java.util.UUID;

public class Match {

    private final UUID userId;
    private final UUID postId;
    private final int offerId;

    public Match(final UUID userId, final UUID postId, final int offerId) {
        this.userId = userId;
        this.postId = postId;
        this.offerId = offerId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getPostId() {
        return postId;
    }

    public int getOfferId() {
        return offerId;
    }
}
