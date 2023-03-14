package com.globallogic.userauth.model.response;

import com.globallogic.userauth.model.Phone;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * POJO that represents a new registration response
 *
 * @since 1.0.0
 */
public class UserRegistrationResponse {

    private final UUID id;

    private final String name;

    private final String email;

    private final List<Phone> phones;

    private final LocalDateTime created;

    private final LocalDateTime lastLogin;

    private final String token;

    private final boolean isActive;

    public UserRegistrationResponse(UUID id, String name, String email, List<Phone> phones, LocalDateTime created, LocalDateTime lastLogin, String token, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phones = phones;
        this.created = created;
        this.lastLogin = lastLogin;
        this.token = token;
        this.isActive = isActive;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public String getToken() {
        return token;
    }

    public boolean isActive() {
        return isActive;
    }
}
