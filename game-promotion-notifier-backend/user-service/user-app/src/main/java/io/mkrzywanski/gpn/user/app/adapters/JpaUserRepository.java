package io.mkrzywanski.gpn.user.app.adapters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity getByUniqueId(UUID uniqueId);
}
