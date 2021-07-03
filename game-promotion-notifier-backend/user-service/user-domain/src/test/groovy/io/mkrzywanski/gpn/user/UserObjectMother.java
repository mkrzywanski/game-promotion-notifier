package io.mkrzywanski.gpn.user;

class UserObjectMother {

    private UserId userId = UserId.of("7cdf83c2-1eed-482e-a80f-1d9423be758f");
    private String userName = "testUsername";
    private EmailAddress emailAddress = EmailAddress.of("test@gmail.com");
    private String firstName = "Michal";

    private UserObjectMother() {
    }

    static UserObjectMother user() {
        return new UserObjectMother();
    }

    UserObjectMother withUserName(final String userName) {
        this.userName = userName;
        return this;
    }

    UserObjectMother withEmail(final EmailAddress email) {
        this.emailAddress = email;
        return this;
    }

    UserObjectMother withFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    UserObjectMother withUserId(final UserId userId) {
        this.userId = userId;
        return this;
    }

    User build() {
        return User.builder()
                .emailAddress(emailAddress)
                .username(userName)
                .userId(userId)
                .firstName(firstName)
                .build();
    }
}
