package io.mkrzywanski.keycloak.listeners;

class UserServicePropertiesProvider {

    static final String USER_SERVICE_URL_ENV = "USER_SERVICE_URL";

    static String getUrl() {
        final String userServiceUrl = System.getenv(USER_SERVICE_URL_ENV);
        if (userServiceUrl == null) {
            throw new IllegalStateException("Environment variable USER_SERVICE_URL was not provided");
        }
        return userServiceUrl;
    }
}
