package io.mkrzywanski.pn.user.app

import groovy.transform.PackageScope
import io.mkrzywanski.pn.user.app.api.CreateUserRequest

@PackageScope
class CreateUserRequestBuilder {

    private UUID userId = UUID.fromString('80b3563b-626d-47ef-b288-327330492f1c')
    private String firstName = "firstName"
    private String lastName = "lastName"
    private String userName = "userName"
    private String email = "test@test.pl"

    static CreateUserRequestBuilder newInstance() {
        new CreateUserRequestBuilder()
    }

    CreateUserRequestBuilder userId(UUID uuid) {
        this.userId = uuid
        this
    }

    CreateUserRequestBuilder firstName(String firstName) {
        this.firstName = firstName
        this
    }

    CreateUserRequestBuilder lastName(String lastName) {
        this.lastName = lastName
        this
    }

    CreateUserRequestBuilder userName(String userName) {
        this.userName = userName
        this
    }


    CreateUserRequestBuilder email(String email) {
        this.email = email
        this
    }

    CreateUserRequest build() {
        new CreateUserRequest(userId, firstName, lastName, userName, email)
    }
}
