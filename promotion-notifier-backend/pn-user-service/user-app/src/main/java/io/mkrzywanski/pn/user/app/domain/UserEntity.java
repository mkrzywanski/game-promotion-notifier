package io.mkrzywanski.pn.user.app.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.UUID;

@Entity
@Getter
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

    public UserEntity(final UUID uniqueId, final String userName, final String firstName, final String email) {
        this.uniqueId = uniqueId;
        this.userName = userName;
        this.firstName = firstName;
        this.email = email;
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
