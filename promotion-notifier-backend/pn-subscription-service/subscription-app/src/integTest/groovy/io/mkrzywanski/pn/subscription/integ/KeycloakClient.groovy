package io.mkrzywanski.pn.subscription.integ

class KeycloakClient {
    String clientId
    String clientSecret

    KeycloakClient(final String clientId, final String clientSecret) {
        this.clientId = clientId
        this.clientSecret = clientSecret
    }
}
