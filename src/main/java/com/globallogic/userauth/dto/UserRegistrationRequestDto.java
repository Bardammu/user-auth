package com.globallogic.userauth.dto;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * POJO that represents a user registration request
 *
 * @since 1.0.0
 */
public class UserRegistrationRequestDto {

    private final String name;

    private final String email;

    private final String password;

    private final List<PhoneDto> phones;

    public UserRegistrationRequestDto(String name, String email, String password, List<PhoneDto> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones != null ? phones : emptyList();
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

    public List<PhoneDto> getPhones() {
        return phones;
    }
}
