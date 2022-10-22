package io.mkrzywanski.pn.matching.matchedoffers;

import java.util.List;

public interface MatchesRepository {
    void saveAll(List<UserOfferMatches> userOfferMatches);
    void saveOrUpdate(List<UserOfferMatches> userOfferMatches);
    List<UserOfferMatches> findAll();
    void removeAlreadyNotifiedPosts(List<UserOfferMatches> all);
}
