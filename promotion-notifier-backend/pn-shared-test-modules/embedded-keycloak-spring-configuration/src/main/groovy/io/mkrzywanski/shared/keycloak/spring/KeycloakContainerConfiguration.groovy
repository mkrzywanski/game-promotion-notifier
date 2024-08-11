package io.mkrzywanski.shared.keycloak.spring

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.mkrzywanski.shared.keycloak.KeyCloakAccess;

import io.mkrzywanski.shared.keycloak.KeyCloakProperties;
import io.mkrzywanski.shared.keycloak.KeycloakClient;
import io.mkrzywanski.shared.keycloak.KeycloakUser
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RealmRepresentation
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakContainerConfiguration {

    @Bean
    KeycloakContainer keyCloakContainer(final KeyCloakProperties keyCloakProperties) {
        KeycloakContainer keyCloakContainer = new KeycloakContainer()
                .withAdminUsername("admin")
                .withAdminPassword("admin")
        keyCloakContainer.start()
        setupKeycloak(keyCloakProperties, keyCloakContainer)
        keyCloakContainer
    }

    @Bean
    KeyCloakProperties keyCloakProperties() {
        def client = new KeycloakClient("test-client", "secret")
        def user = new KeycloakUser("test", "test")
        def admin = new KeycloakUser("admin", "admin")
        new KeyCloakProperties(client, KeyCloakProperties.ADMIN_CLI_CLIENT, user, admin, "xD")
    }

    @Bean
    KeyCloakAccess keycloak(KeyCloakProperties keyCloakProperties, KeycloakContainer keyCloakContainer) {
        def adminAccess = keyCloakContainer.keycloakAdminClient
        def userAccess = KeycloakBuilder.builder()
                .serverUrl(keyCloakContainer.authServerUrl)
                .realm(keyCloakProperties.testRealm)
                .clientId(keyCloakProperties.client.clientId)
                .clientSecret(keyCloakProperties.client.clientSecret)
                .username(keyCloakProperties.user.username)
                .password(keyCloakProperties.user.password)
                .build()
        new KeyCloakAccess(adminAccess, userAccess)
    }

    static def setupKeycloak(KeyCloakProperties keyCloakProperties, KeycloakContainer keycloakContainer) {
        def keycloak = keycloakContainer.keycloakAdminClient;

        def realm = testRealm(keyCloakProperties.testRealm)
        keycloak.realms().create(realm)

        def clientRepresentation = testClient(keyCloakProperties.client)
        keycloak.realm(keyCloakProperties.testRealm).clients().create(clientRepresentation)

        def user = testUser(keyCloakProperties.user)
        keycloak.realm(keyCloakProperties.testRealm).users().create(user)

    }

    private static UserRepresentation testUser(KeycloakUser keycloakUser) {
        CredentialRepresentation credential = new CredentialRepresentation()
        credential.type = CredentialRepresentation.PASSWORD
        credential.value = keycloakUser.password

        UserRepresentation user = new UserRepresentation()
        user.username = keycloakUser.username
        user.firstName = "test"
        user.lastName = "test"
        user.email = "test@gmail.com"
        user.credentials = Arrays.asList(credential)
        user.enabled = true
        user.realmRoles = Arrays.asList("admin")
        user
    }

    private static def testRealm(String realm) {
        def rep = new RealmRepresentation()
        rep.realm = realm
        rep.enabled = true
        rep
    }

    private static def testClient(KeycloakClient keycloakClient) {
        def clientRepresentation = new ClientRepresentation()
        clientRepresentation.clientId = keycloakClient.clientId
        clientRepresentation.secret = keycloakClient.clientSecret
        clientRepresentation.redirectUris = Arrays.asList("*")
        clientRepresentation.directAccessGrantsEnabled = true
        clientRepresentation.standardFlowEnabled = true
        clientRepresentation.serviceAccountsEnabled = true
        clientRepresentation
    }
}
