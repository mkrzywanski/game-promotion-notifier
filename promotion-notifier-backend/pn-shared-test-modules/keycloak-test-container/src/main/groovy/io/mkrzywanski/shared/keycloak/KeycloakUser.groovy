package io.mkrzywanski.shared.keycloak;

class KeycloakUser {
    String username
    String password

    KeycloakUser(final String username, final String password) {
        this.username = username
        this.password = password
    }
}
