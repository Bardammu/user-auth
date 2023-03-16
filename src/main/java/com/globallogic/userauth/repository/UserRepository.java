package com.globallogic.userauth.repository;

import com.globallogic.userauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * {@link JpaRepository} for {@link User}
 *
 * @since 1.0.0
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
