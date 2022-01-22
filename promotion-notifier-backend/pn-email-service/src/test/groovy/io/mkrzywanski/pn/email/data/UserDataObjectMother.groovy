package io.mkrzywanski.pn.email.data


import io.mkrzywanski.pn.email.api.UserData

final class UserDataObjectMother {
    private UUID id = UUID.fromString("e1b8b415-4f3f-4cdc-a984-344b7c1e3585")
    private String username = "username"
    private String firstname = "Andrew"
    private String email = EmailObjectMother.EMAIL

    private UserDataObjectMother() {
    }

    static UserDataObjectMother userData() {
        new UserDataObjectMother()
    }

    static UserData withUsername(String username) {
        userData().username(username).build()
    }

    static UserData withFirstName(String firstName) {
        userData().firstName(firstName).build()
    }

    static UserData withEmail(String email) {
        userData().email(email).build()
    }

    UserDataObjectMother username(String username) {
        this.username = username
        this
    }

    UserDataObjectMother firstName(String firstName) {
        this.firstname = firstName
        this
    }

    UserDataObjectMother email(String email) {
        this.email = email
        this
    }

    UserDataObjectMother id(UUID id) {
        this.id = id
        this
    }

    UserData build() {
        return new UserData(id, username, firstname, email)
    }
}
