package io.mkrzywanski.pn.subscription.app.adapters;

import io.mkrzywanski.pn.subscription.MatchingRequest;
import io.mkrzywanski.pn.subscription.SubscriptionMatchingResult;
import io.mkrzywanski.pn.subscription.app.api.CreateSubscriptionRequest;
import io.mkrzywanski.pn.subscription.app.api.SubscriptionCreatedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/subscriptions")
public class SubscriptionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionEndpoint.class);

    private final SubscriptionFacade subscriptionFacade;

    public SubscriptionEndpoint(final SubscriptionFacade subscriptionFacade) {
        this.subscriptionFacade = subscriptionFacade;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscriptionCreatedResponse> create(@Validated @RequestBody final CreateSubscriptionRequest createSubscriptionRequest) {
        final SubscriptionCreatedResponse subscriptionCreatedResponse = subscriptionFacade.create(createSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionCreatedResponse);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, path = "/match")
    public ResponseEntity<SubscriptionMatchingResult> match(@RequestBody final MatchingRequest matchingRequest) {
        final SubscriptionMatchingResult match = subscriptionFacade.match(matchingRequest);
        return ResponseEntity.ok(match);
    }

}
