package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Document(collection = "#{@environment.getProperty('gpn.matching-service.mongodb.collectionName')}")
@Value
public class UserOfferMatches {
    @Id
    UUID userId;
    Set<PostEntity> postEntities;

    static UserOfferMatches newInstance(final Set<PostEntity> postEntitySet) {
        return new UserOfferMatches(UUID.randomUUID(), postEntitySet);
    }

    boolean hasPosts() {
        return !postEntities.isEmpty();
    }

    public Set<UUID> getPostIds() {
        return postEntities.stream().map(PostEntity::getPostId).collect(Collectors.toSet());
    }
}
