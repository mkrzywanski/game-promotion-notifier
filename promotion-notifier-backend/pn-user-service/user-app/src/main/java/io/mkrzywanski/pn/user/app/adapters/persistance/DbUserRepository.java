package io.mkrzywanski.pn.user.app.adapters.persistance;

import io.mkrzywanski.gpn.user.NewUserDetails;
import io.mkrzywanski.gpn.user.User;
import io.mkrzywanski.gpn.user.UserId;
import io.mkrzywanski.gpn.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class DbUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public DbUserRepository(final JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public UserId save(final NewUserDetails createUserInfo) {
        UserEntity user = UserEntity.Builder.userEntity()
                .withUniqueId(UUID.randomUUID())
                .withUsername(createUserInfo.getUserName())
                .withFirstname(createUserInfo.getFirstName())
                .withEmail(createUserInfo.getEmail().asString())
                .build();
        user = jpaUserRepository.save(user);
        return UserId.of(user.getUniqueId());
    }

    @Override
    public Optional<User> getById(final UserId userId) {
        return jpaUserRepository.getByUniqueId(userId.asUuid())
                .map(UserEntity::toDomain);
    }
}
