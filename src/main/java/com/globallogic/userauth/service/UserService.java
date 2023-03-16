package com.globallogic.userauth.service;

import com.globallogic.userauth.dto.UserRegistrationRequestDto;
import com.globallogic.userauth.dto.UserResponseDto;
import com.globallogic.userauth.exception.UserAlreadyExistException;
import com.globallogic.userauth.model.User;

/**
 * Service to manage User registration
 *
 * @since 1.0.0
 */
public interface UserService {

    /**
     * Register a new user
     *
     * @param userRegistrationRequestDto the information of the new {@link User} to be register
     * @return the information of the registered user
     * @throws UserAlreadyExistException if the user is already registered
     */
    UserResponseDto registerNewUser(UserRegistrationRequestDto userRegistrationRequestDto);

    /**
     * Get all the information of a registered {@link User}
     *
     * @param email that identifies the {@link User}
     * @return the {@link User}
     */
    UserResponseDto getUser(String email);
}
