package io.mkrzywanski.gpn.user;

public interface UserRepository {
    UserId save(NewUserDetails newUserDetails);

    User getById(UserId userId);
}
