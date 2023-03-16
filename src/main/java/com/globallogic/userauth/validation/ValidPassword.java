package com.globallogic.userauth.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.globallogic.userauth.validation.Errors.INVALID_PASSWORD_DETAILS;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom annotation that can be used valid passwords
 *
 * @since 1.0.0
 */
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface ValidPassword {

    String message() default INVALID_PASSWORD_DETAILS;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
