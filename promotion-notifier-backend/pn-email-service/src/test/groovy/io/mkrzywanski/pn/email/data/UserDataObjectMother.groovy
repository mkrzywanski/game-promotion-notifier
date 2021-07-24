package io.mkrzywanski.pn.email.data


import io.mkrzywanski.pn.email.api.UserData

final class UserDataObjectMother {
    private String username = "username"
    private String name = "Andrew"

    private UserDataObjectMother() {
    }

    static UserDataObjectMother userData() {
        return new UserDataObjectMother()
    }

    static UserData withUsername(String username) {
        return userData().username(username).build()
    }

    static UserData withName(String name) {
        return userData().name(name).build()
    }

    UserDataObjectMother username(String username) {
        this.username = username
        return this
    }

    UserDataObjectMother name(String name) {
        this.name = name
        return this
    }

    UserData build() {
        return new UserData(name, username)
    }
}
