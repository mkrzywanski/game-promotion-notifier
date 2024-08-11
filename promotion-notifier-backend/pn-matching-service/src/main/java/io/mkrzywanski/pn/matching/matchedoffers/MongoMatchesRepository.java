package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Component
class MongoMatchesRepository implements MatchesRepository {

    private final String collectionName;
    private final MongoTemplate mongoTemplate;

    MongoMatchesRepository(final MongoTemplate mongoTemplate,
                                  @Value("${gpn.matching-service.mongodb.collectionName}") final String collectionName) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
    }

    @Override
    public void saveAll(final List<UserOfferMatches> userOfferMatches) {
        userOfferMatches.forEach(mongoTemplate::save);
    }

    @Override
    public void saveOrUpdate(final List<UserOfferMatches> userOfferMatches) {
        for (UserOfferMatches userOfferMatches1 : userOfferMatches) {
            final var byUserIdQuery = Query.query(where("userId").is(userOfferMatches1.getUserId()));
            final var update = new Update().addToSet("postEntities").each(userOfferMatches1.getPostEntities());
            final var upsert = mongoTemplate.upsert(byUserIdQuery, update, UserOfferMatches.class);
            final long matchedCount = upsert.getMatchedCount();
            log.info("Upsert done {}", matchedCount);
        }
    }

    @Override
    public List<UserOfferMatches> findAll() {
        return mongoTemplate.findAll(UserOfferMatches.class);
    }

    @Override
    public void removeAlreadyNotifiedPosts(final List<UserOfferMatches> matches) {
        final var postIds = matches.stream()
                .map(UserOfferMatches::getPostIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        final var update = new Update().pull("postEntities", Query.query(where("postId").in(postIds)));

        mongoTemplate.updateMulti(new Query(), update, UserOfferMatches.class);
    }
}
