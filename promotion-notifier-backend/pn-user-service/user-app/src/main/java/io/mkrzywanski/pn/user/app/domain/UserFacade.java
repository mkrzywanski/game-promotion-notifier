package io.mkrzywanski.pn.user.app.domain;

import io.mkrzywanski.pn.user.app.api.CreateUserRequest;
import io.mkrzywanski.pn.user.app.api.UserCreatedResponse;
import io.mkrzywanski.pn.user.app.api.UserDetailsResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserFacade {

    private final JpaUserRepository userRepository;

    public UserFacade(final JpaUserRepository repository) {
        this.userRepository = repository;
    }

    public UserCreatedResponse create(final CreateUserRequest createUserRequest) {
        UserEntity user = UserEntity.Builder.userEntity()
                .withUniqueId(createUserRequest.getUserId())
                .withUsername(createUserRequest.getUserName())
                .withFirstname(createUserRequest.getFirstName())
                .withLastName(createUserRequest.getLastName())
                .withEmail(createUserRequest.getEmail())
                .build();
        user = userRepository.save(user);
        return new UserCreatedResponse(user.getUniqueId().toString());
    }

    public Optional<UserDetailsResponse> get(final UUID uuid) {
        return userRepository.getByUniqueId(uuid)
                .map(UserFacade::from);
    }

    public static UserDetailsResponse from(final UserEntity user) {
        return UserDetailsResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .username(user.getUserName())
                .userId(user.getUniqueId())
                .build();
    }
}
