package io.mkrzywanski.pn.user.app.domain;

import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.util.UUID;

@Entity
@Getter
class UserEntity {

    @Id
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private Long id;

    private UUID uniqueId;

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private UserEntity() {
    }

    UserEntity(final UUID uniqueId, final String userName, final String firstName, final String lastName, final String email) {
        this.uniqueId = uniqueId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    static class Builder {

        private UUID uniqueId;
        private String userName;
        private String firstName;
        private String lastName;
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

        Builder withLastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        Builder withEmail(final String email) {
            this.email = email;
            return this;
        }

        UserEntity build() {
            return new UserEntity(uniqueId, userName, firstName, lastName, email);
        }


    }
}
