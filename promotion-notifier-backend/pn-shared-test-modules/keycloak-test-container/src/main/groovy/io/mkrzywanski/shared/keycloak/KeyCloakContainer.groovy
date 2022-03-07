package io.mkrzywanski.shared.keycloak

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

class KeyCloakContainer extends GenericContainer<KeyCloakContainer> {

    private static final DockerImageName KEYCLOAK_IMAGE = DockerImageName.parse("jboss/keycloak:16.1.1")
    private static final int PORT = 8080

    private KeycloakUser admin;

    KeyCloakContainer(KeycloakUser admin) {
        super(KEYCLOAK_IMAGE)
        this.admin = admin
    }

    @Override
    protected void configure() {
        withEnv("KEYCLOAK_HTTP_PORT", String.valueOf(PORT))
        withEnv("KEYCLOAK_USER", admin.username)
        withEnv("KEYCLOAK_PASSWORD", admin.password)
        withExposedPorts(PORT)
        waitingFor(Wait.forLogMessage(".*Keycloak (.*) started in.*", 1))
    }

    @Override
    void close() {
        this.stop()
    }
}
