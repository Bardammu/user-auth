package com.globallogic.userauth.repository;

import com.globallogic.userauth.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * {@link JpaRepository} for {@link Phone}
 *
 * @since 1.0.0
 */
public interface PhoneRepository extends JpaRepository<Phone, UUID> {
}
