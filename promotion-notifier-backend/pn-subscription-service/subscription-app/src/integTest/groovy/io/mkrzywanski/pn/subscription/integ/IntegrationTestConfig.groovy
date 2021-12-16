package io.mkrzywanski.pn.subscription.integ


import io.mkrzywanski.pn.subscription.app.SubscriptionServiceApp
import org.elasticsearch.client.RestHighLevelClient
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RealmRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.SupplierJwtDecoder
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

@Configuration
@Import(SubscriptionServiceApp)
class IntegrationTestConfig {

    public static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:7.13.2"

    @Bean(destroyMethod = "")
    ElasticsearchContainer elasticsearchContainer() {
        def container = new ElasticsearchContainer(DockerImageName.parse(ELASTICSEARCH_IMAGE))
                .withEnv("discovery.type", "single-node")
                .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
                .withReuse(true)
        container.start()
        container
    }

    @Bean
    @Primary
    RestHighLevelClient testElasticsearchClient() throws UnknownHostException {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:${elasticsearchContainer().getFirstMappedPort()}")
                .build();

        RestClients.create(clientConfiguration).rest()
    }

    @Bean
    KeyCloakContainer keyCloakContainer(final KeyCloakProperties keyCloakProperties) {
        KeyCloakContainer keyCloakContainer = new KeyCloakContainer(keyCloakProperties.adminUser)
        keyCloakContainer.start()
        setupKeycloak(keyCloakProperties, keyCloakContainer.getFirstMappedPort())
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
    SupplierJwtDecoder jwtDecoderByIssuerUri(KeyCloakContainer keyCloakContainer, KeyCloakProperties keyCloakProperties) {
        return new SupplierJwtDecoder(() -> JwtDecoders.fromIssuerLocation("http://localhost:${keyCloakContainer.firstMappedPort}/auth/realms/${keyCloakProperties.testRealm}"));
    }

    @Bean
    KeyCloakAccess keycloak(KeyCloakProperties keyCloakProperties, KeyCloakContainer keyCloakContainer) {
        def adminAccess = KeycloakBuilder.builder()
                .serverUrl("http://localhost:${keyCloakContainer.getFirstMappedPort()}/auth")
                .realm("master")
                .clientId(keyCloakProperties.adminCliClient.clientId)
                .username(keyCloakProperties.adminUser.username)
                .password(keyCloakProperties.adminUser.password)
                .build()
        def userAccess = KeycloakBuilder.builder()
                .serverUrl("http://localhost:${keyCloakContainer.getFirstMappedPort()}/auth")
                .realm(keyCloakProperties.testRealm)
                .clientId(keyCloakProperties.client.clientId)
                .clientSecret(keyCloakProperties.client.clientSecret)
                .username(keyCloakProperties.user.username)
                .password(keyCloakProperties.user.password)
                .build()
        new KeyCloakAccess(adminAccess, userAccess)
    }

    def setupKeycloak(KeyCloakProperties keyCloakProperties,
                      int port) {
        def keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:${port}/auth")
                .realm("master")
                .clientId(keyCloakProperties.adminCliClient.clientId)
                .username(keyCloakProperties.adminUser.username)
                .password(keyCloakProperties.adminUser.password)
                .build()

        def realm = testRealm(keyCloakProperties.testRealm)
        keycloak.realms().create(realm)

        def clientRepresentation = testClient(keyCloakProperties.client)
        keycloak.realm(keyCloakProperties.testRealm).clients().create(clientRepresentation)

        def user = testUser(keyCloakProperties.user)
        keycloak.realm(keyCloakProperties.testRealm).users().create(user)

    }

    private UserRepresentation testUser(KeycloakUser keycloakUser) {
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

    private def testRealm(String realm) {
        def rep = new RealmRepresentation()
        rep.realm = realm
        rep.enabled = true
        rep
    }

    private def testClient(KeycloakClient keycloakClient) {
        def clientRepresentation = new ClientRepresentation()
        clientRepresentation.clientId = keycloakClient.clientId
        clientRepresentation.secret = keycloakClient.clientSecret
        clientRepresentation.redirectUris = Arrays.asList("*")
        clientRepresentation.directAccessGrantsEnabled = true
        clientRepresentation.standardFlowEnabled = true
        clientRepresentation
    }

}
