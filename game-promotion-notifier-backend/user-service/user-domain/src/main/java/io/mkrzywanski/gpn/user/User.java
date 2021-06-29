package io.mkrzywanski.gpn.user;

public class User {

    private final UserId userId;
    private final String username;
    private final String firstName;
    private final EmailAddress emailAddress;

    public User(final UserId userId, final String username, final String firstName, final EmailAddress emailAddress) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.emailAddress = emailAddress;
    }

    UserId getUserId() {
        return userId;
    }

    String getUsername() {
        return username;
    }

    String getFirstName() {
        return firstName;
    }

    EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public static class Builder {

        private UserId userId;
        private String username;
        private String firstName;
        private EmailAddress emailAddress;

        private Builder() {
        }

        public static Builder user() {
            return new Builder();
        }

        public Builder withUserId(final UserId userId) {
            this.userId = userId;
            return this;
        }

        public Builder withFirstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withUsername(final String username) {
            this.username = username;
            return this;
        }

        public Builder withEmail(final EmailAddress email) {
            this.emailAddress = email;
            return this;
        }

        public User build() {
            return new User(userId, username, firstName, emailAddress);
        }
    }
}
