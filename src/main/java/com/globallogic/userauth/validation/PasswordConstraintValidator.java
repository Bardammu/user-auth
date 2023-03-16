package com.globallogic.userauth.validation;

import org.passay.AllowedRegexRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.asList;

/**
 * Implementation of {@link ConstraintValidator} that can be used in conjunction with {@link ValidPassword} to
 * validate that a password is valid
 *
 * @since 1.0.0
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(asList(
                new LengthRule(8, 12),
                // password contains exactly one uppercase letter
                new AllowedRegexRule("^[a-z0-9]*[A-Z][a-z0-9]*$"),
                // password contains exactly two digits
                new AllowedRegexRule("^[a-zA-Z]*[0-9][a-zA-Z]*[0-9][a-zA-Z]*$")));

        RuleResult result = validator.validate(new PasswordData(password));

        return result.isValid();
    }
}
