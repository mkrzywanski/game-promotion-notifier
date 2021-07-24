package io.mkrzywanski.gpn.user;

import java.util.Optional;

public interface UserRepository {
    UserId save(NewUserDetails newUserDetails);

    Optional<User> getById(UserId userId);
}
