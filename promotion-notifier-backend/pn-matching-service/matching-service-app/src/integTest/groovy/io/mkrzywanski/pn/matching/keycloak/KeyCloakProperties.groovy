package io.mkrzywanski.pn.matching.keycloak

class KeyCloakProperties {

    static KeycloakClient ADMIN_CLI_CLIENT = new KeycloakClient("admin-cli", null)

    KeycloakClient client
    KeycloakClient adminCliClient
    KeycloakUser user
    KeycloakUser adminUser
    String testRealm

    KeyCloakProperties(final KeycloakClient client,
                       final KeycloakClient adminCliClient,
                       final KeycloakUser user,
                       final KeycloakUser adminUser,
                       final String realm) {
        this.client = client
        this.adminCliClient = adminCliClient
        this.user = user
        this.adminUser = adminUser
        this.testRealm = realm
    }
}
