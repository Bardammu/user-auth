package com.globallogic.userauth.service;

import com.globallogic.userauth.dto.UserRegistrationRequestDto;
import com.globallogic.userauth.dto.UserRegistrationResponseDto;
import com.globallogic.userauth.exception.UserAlreadyExistException;

/**
 * Service to manage User registration
 *
 * @since 1.0.0
 */
public interface UserService {

    /**
     * Register a new user
     *
     * @param userRegistrationRequestDto the information of the new user to be register
     * @return the information of the registered user
     * @throws UserAlreadyExistException if the user is already registered
     */
    UserRegistrationResponseDto registerNewUser(UserRegistrationRequestDto userRegistrationRequestDto);

}
