package com.globallogic.userauth.validation

import spock.lang.Shared
import spock.lang.Specification

import javax.validation.ConstraintValidatorContext

class PasswordConstraintValidatorSpec extends Specification {

    @Shared
    ConstraintValidatorContext context

    @Shared
    PasswordConstraintValidator passwordConstraintValidator

    def setupSpec() {
        context = Mock()
        passwordConstraintValidator = new PasswordConstraintValidator();
    }

    def "test valid passwords"() {
        when: "a valid password is verify"
            def isValid = passwordConstraintValidator.isValid(password, context)

        then: "the result is true"
            isValid

        where:
            password << ["12passworD", "11aaaaaA", "aaaaaaaaaA12"]
    }

    def "test invalid passwords"() {
        when: "an invalid password is verify"
            def isValid = passwordConstraintValidator.isValid(password, context)

        then: "the result is true"
            !isValid

        where:
            password << ["password", "passA10", "passwoRD10", "passwoR100", "passwordworworD10", "10password#A"]
    }
}
