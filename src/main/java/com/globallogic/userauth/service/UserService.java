package com.globallogic.userauth.service;

import com.globallogic.userauth.model.request.UserRegistrationRequest;
import com.globallogic.userauth.model.response.UserRegistrationResponse;
import com.globallogic.userauth.validation.UserAlreadyExistException;

/**
 * Service to manage User registration
 *
 * @since 1.0.0
 */
public interface UserService {

    /**
     * Register a new user
     *
     * @param userRegistrationRequest the information of the new user to be register
     * @return the information of the registered user
     * @throws UserAlreadyExistException if the user is already registered
     */
    UserRegistrationResponse registerNewUser(UserRegistrationRequest userRegistrationRequest);

}
