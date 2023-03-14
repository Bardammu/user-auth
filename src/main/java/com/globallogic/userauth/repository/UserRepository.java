package com.globallogic.userauth.repository;

import com.globallogic.userauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link JpaRepository} for {@link User}
 *
 * @since 1.0.0
 */
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);

}
