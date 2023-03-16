package com.globallogic.userauth.dto;

import com.globallogic.userauth.validation.ValidPassword;

import javax.validation.constraints.Email;
import java.util.List;

import static com.globallogic.userauth.validation.Errors.MALFORMED_EMAIL_ADDRESS_DETAILS;
import static java.util.Collections.emptyList;

/**
 * POJO that represents a user registration request
 *
 * @since 1.0.0
 */
public class UserRegistrationRequestDto {

    private final String name;

    @Email(message = MALFORMED_EMAIL_ADDRESS_DETAILS)
    private final String email;

    @ValidPassword
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
