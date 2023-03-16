package com.globallogic.userauth.exception;

import com.globallogic.userauth.model.User;

/**
 * Custom Exception to be used when a {@link User} is already registered on the database
 *
 * @since 1.0.0
 */
public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String message) {
        super(message);
    }

}
