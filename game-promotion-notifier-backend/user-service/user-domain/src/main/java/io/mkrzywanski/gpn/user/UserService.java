package io.mkrzywanski.gpn.user;

public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserId create(final NewUserDetails newUserDetails) {
        return userRepository.save(newUserDetails);
    }
}
