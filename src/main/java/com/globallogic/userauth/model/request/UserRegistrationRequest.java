package com.globallogic.userauth.model.request;

import com.globallogic.userauth.model.Phone;

import java.util.List;

/**
 * POJO that represents a user registration request
 *
 * @since 1.0.0
 */
public class UserRegistrationRequest {

    private final String name;

    private final String email;

    private final String password;

    private final List<Phone> phones;

    public UserRegistrationRequest(String name, String email, String password, List<Phone> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Phone> getPhones() {
        return phones;
    }
}
