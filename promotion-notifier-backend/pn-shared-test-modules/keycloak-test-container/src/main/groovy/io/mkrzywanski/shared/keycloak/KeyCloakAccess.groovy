package io.mkrzywanski.shared.keycloak

import groovy.transform.PackageScope
import org.keycloak.admin.client.Keycloak

class KeyCloakAccess {

    Keycloak keycloakAdminAccess
    Keycloak keycloakTestUserAccess

    KeyCloakAccess(final Keycloak keycloakAdminAccess, final Keycloak keycloakTestUserAccess) {
        this.keycloakAdminAccess = keycloakAdminAccess
        this.keycloakTestUserAccess = keycloakTestUserAccess
    }

    def getUserToken() {
        keycloakTestUserAccess.tokenManager().accessToken.token
    }
}
