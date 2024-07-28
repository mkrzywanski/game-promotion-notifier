package io.mkrzywanski.keycloak.listeners

import com.github.tomakehurst.wiremock.client.WireMock
import dasniko.testcontainers.keycloak.KeycloakContainer
import io.mkrzywanski.shared.keycloak.KeyCloakAccess
import io.mkrzywanski.shared.keycloak.KeyCloakProperties
import io.mkrzywanski.shared.keycloak.KeycloakClient
import io.mkrzywanski.shared.keycloak.KeycloakUser
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RealmRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.utility.MountableFile
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath
import static io.mkrzywanski.test.hamcrest.IsUuid.isUUID
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo
import static org.testcontainers.shaded.org.awaitility.Awaitility.await

class ListenerTest extends Specification {

    static def VALID_REDIRECT_URI = "http://localhost:5555"
    def network = Network.newNetwork()
    def properties = keyCloakProperties()
    def keycloak = keyCloakContainer(properties, network)
    def access

    def wiremock = new GenericContainer("wiremock/wiremock:2.32.0")
            .withExposedPorts(8080)
            .withNetwork(network)
            .withNetworkAliases("wiremock")
    WireMock wiremockClient

    def selenium = new BrowserWebDriverContainer<>()
            .withCapabilities(new ChromeOptions())
            .withNetwork(network)
    def driver

    void setup() {
        wiremock.start()
        wiremockClient = new WireMock("localhost", wiremock.getFirstMappedPort())

        keycloak.withEnv(UserServicePropertiesProvider.USER_SERVICE_URL_ENV, "http://wiremock:8080")
        keycloak.portBindings = List.of("8080:8080")
        keycloak.start()
        access = keycloak(properties, keycloak)
        setupKeycloak(properties, keycloak.keycloakAdminClient)

        selenium.start()
        driver = new RemoteWebDriver(selenium.getSeleniumAddress(), new ChromeOptions())

    }

    void cleanup() {
        wiremock.stop()
        keycloak.close()
        selenium.close()
    }

    def "should call user service when user is registered"() {
        given:
        listenerIsEnabled()

        when:
        registrationIsPerformed()

        then:
        userServiceHasBeenCalledByListener()
        and:
        receivedRequestBodyIsCorrect()
    }

    private void userServiceHasBeenCalledByListener() {
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> wiremockClient.verifyThat(1, postRequestedFor(urlEqualTo("/v1/users"))))
    }

    private void receivedRequestBodyIsCorrect() {
        def body = wiremockClient.serveEvents.get(0).request.bodyAsString
        assertThat(body, hasJsonPath('$.userId', isUUID()))
        assertThat(body, hasJsonPath('$.userName', equalTo("test")))
        assertThat(body, hasJsonPath('$.firstName', equalTo("test")))
        assertThat(body, hasJsonPath('$.lastName', equalTo("test")))
        assertThat(body, hasJsonPath('$.email', equalTo("test@test.pl")))

    }

    def listenerIsEnabled() {
        RealmResource realmResource = access.keycloakAdminAccess.realm(properties.testRealm)
        def config = realmResource.realmEventsConfig
        def types = config.eventsListeners
        types += "UserRegisteredListenerProviderFactory"
        config.eventsListeners = types
        realmResource.updateRealmEventsConfig(config)
    }

    def registrationIsPerformed() {
        driver.navigate().to("http://keycloak:8080/realms/${properties.testRealm}/protocol/openid-connect/auth?response_type=code&client_id=${properties.client.clientId}&scope=openid&redirect_uri=${VALID_REDIRECT_URI}")

        driver.findElement(By.linkText("Register")).click()

        driver.findElement(By.id("firstName")).sendKeys("test")
        driver.findElement(By.id("lastName")).sendKeys("test")
        driver.findElement(By.id("email")).sendKeys("test@test.pl")
        driver.findElement(By.id("username")).sendKeys("test")
        driver.findElement(By.id("password")).sendKeys("test")
        driver.findElement(By.id("password-confirm")).sendKeys("test")
        driver.findElement(By.cssSelector("input[type='submit']")).submit()
    }

    KeycloakContainer keyCloakContainer(final KeyCloakProperties keyCloakProperties, final Network network) {
        def listenerJarPath = new File("build/libs").listFiles().first().path
        new KeycloakContainer()
                .withCopyFileToContainer(MountableFile.forHostPath(listenerJarPath, 0744), "/opt/keycloak/providers/user-registered-listener.jar")
                .withNetwork(network)
                .withNetworkAliases("keycloak")
    }

    KeyCloakProperties keyCloakProperties() {
        def client = new KeycloakClient("test-client", "secret")
        def user = new KeycloakUser("test", "test")
        def admin = new KeycloakUser("admin", "admin")
        new KeyCloakProperties(client, KeyCloakProperties.ADMIN_CLI_CLIENT, user, admin, "test")
    }

    KeyCloakAccess keycloak(KeyCloakProperties keyCloakProperties, KeycloakContainer keyCloakContainer) {
        def adminAccess = keyCloakContainer.keycloakAdminClient
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

    static def setupKeycloak(KeyCloakProperties keyCloakProperties, Keycloak keycloak) {

        def realm = testRealm(keyCloakProperties.testRealm)
        keycloak.realms().create(realm)

        def clientRepresentation = testClient(keyCloakProperties.client)
        keycloak.realm(keyCloakProperties.testRealm).clients().create(clientRepresentation)

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
        rep.registrationAllowed = true
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
        clientRepresentation.implicitFlowEnabled = true
        clientRepresentation.redirectUris = List.of(VALID_REDIRECT_URI)
        clientRepresentation
    }
}
