package com.globallogic.userauth.validation;

/**
 * List of error codes an error message used on responses to invalid request
 *
 * @since 1.0.0
 */
public final class Errors {

    public static final int MALFORMED_JSON_REQUEST_CODE = 1;

    public static final int EMAIL_ALREADY_REGISTERED_CODE = 2;

    public static final String MALFORMED_EMAIL_ADDRESS_DETAILS = "malformed email address";

    public static final String INVALID_PASSWORD_DETAILS = "invalid password";

    public static final String EMAIL_ALREADY_REGISTERED_DETAILS = "email is already in used";

}
