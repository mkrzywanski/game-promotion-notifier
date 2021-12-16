package io.mkrzywanski.pn.subscription.app.adapters;

import io.mkrzywanski.pn.subscription.MatchingRequest;
import io.mkrzywanski.pn.subscription.SubscriptionMatchingResult;
import io.mkrzywanski.pn.subscription.app.api.CreateSubscriptionRequest;
import io.mkrzywanski.pn.subscription.app.api.SubscriptionCreatedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/subscriptions")
class SubscriptionEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionEndpoint.class);

    private final SubscriptionFacade subscriptionFacade;

    SubscriptionEndpoint(final SubscriptionFacade subscriptionFacade) {
        this.subscriptionFacade = subscriptionFacade;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscriptionCreatedResponse> create(@Validated @RequestBody final CreateSubscriptionRequest createSubscriptionRequest) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        final SubscriptionCreatedResponse subscriptionCreatedResponse = subscriptionFacade.create(createSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionCreatedResponse);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("/match")
    public ResponseEntity<SubscriptionMatchingResult> match(@RequestBody final MatchingRequest matchingRequest) {
        final SubscriptionMatchingResult match = subscriptionFacade.match(matchingRequest);
        return ResponseEntity.ok(match);
    }

}
