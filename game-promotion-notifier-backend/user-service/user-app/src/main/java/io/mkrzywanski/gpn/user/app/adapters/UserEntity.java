package io.mkrzywanski.gpn.user.app.adapters;

import io.mkrzywanski.gpn.user.EmailAddress;
import io.mkrzywanski.gpn.user.User;
import io.mkrzywanski.gpn.user.UserId;

import javax.persistence.*;
import java.util.UUID;

import static io.mkrzywanski.gpn.user.User.Builder.*;

@Entity
public class UserEntity {

    @Id
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private Long id;

    private UUID uniqueId;

    private String userName;

    private String firstName;

    private String email;

    private UserEntity() {
    }

    private UserEntity(final UUID uniqueId, final String userName, final String firstName, final String email) {
        this.uniqueId = uniqueId;
        this.userName = userName;
        this.firstName = firstName;
        this.email = email;
    }

    Long getId() {
        return id;
    }

    String getUserName() {
        return userName;
    }

    String getFirstName() {
        return firstName;
    }

    String getEmail() {
        return email;
    }

    UUID getUniqueId() {
        return uniqueId;
    }

    User toDomain() {
        return user()
                .withUserId(UserId.of(uniqueId))
                .withUsername(userName)
                .withFirstName(firstName)
                .withEmail(EmailAddress.of(email))
                .build();
    }

    static class Builder {

        private UUID uniqueId;
        private String userName;
        private String firstName;
        private String email;

        static Builder userEntity() {
            return new Builder();
        }

        Builder withUniqueId(final UUID uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        Builder withUsername(final String username) {
            this.userName = username;
            return this;
        }

        Builder withFirstname(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        Builder withEmail(final String email) {
            this.email = email;
            return this;
        }

        UserEntity build() {
            return new UserEntity(uniqueId, userName, firstName, email);
        }


    }
}
