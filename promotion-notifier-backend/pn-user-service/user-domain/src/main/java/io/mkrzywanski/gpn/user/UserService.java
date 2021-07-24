package io.mkrzywanski.gpn.user;

import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserId create(final NewUserDetails newUserDetails) {
        return userRepository.save(newUserDetails);
    }

    public Optional<User> get(final UserId userId) {
        return userRepository.getById(userId);
    }
}
