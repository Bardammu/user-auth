package com.globallogic.userauth.integration

import com.globallogic.userauth.dto.ErrorMessageResponseDto
import com.globallogic.userauth.dto.UserRegistrationRequestDto
import com.globallogic.userauth.dto.UserRegistrationResponseDto
import org.springframework.http.HttpEntity

import java.time.LocalDateTime

import static com.globallogic.userauth.validation.Errors.*
import static org.springframework.http.HttpStatus.*

class UserRegistrationSpec extends IntegrationSpec {

    def "register a new user with valid data"() {
        when: "when post is performed with the data of a new user"
            def requestEntity = new HttpEntity<>(newUserRequest)
            def responseEntity = restTemplate.postForEntity(URL_SIGNUP, requestEntity , UserRegistrationResponseDto.class)

        then: "status code is 202"
            responseEntity.statusCode == CREATED

        and: "the response body contains the user data"
            verifyAll(responseEntity.getBody()) {
                name ==  newUserRequest.getName()
                email == newUserRequest.getEmail()
                phones == newUserRequest.getPhones()
                created < LocalDateTime.now()
                lastLogin < LocalDateTime.now()
                jwtTokenManager.getEmailFromToken(token) == newUserRequest.getEmail()
                isActive
            }
    }

    def "get an error when attempt to register with an invalid email"() {
        given: "the data of a new user"
            def userRegistrationRequest = new UserRegistrationRequestDto("john", "doemail.com", "passworD10", null)

        when: "when post is performed with the data of the new user"
            def requestEntity = new HttpEntity<>(userRegistrationRequest)
            def responseEntity = restTemplate.postForEntity(URL_SIGNUP, requestEntity, ErrorMessageResponseDto.class)

        then: "the status code is 400"
            responseEntity.statusCode == BAD_REQUEST

        and: "the response body contains an error message"
            verifyAll(responseEntity.getBody()) {
                errorMessages.size() == 1
                errorMessages.get(0).getTimestamp() < LocalDateTime.now()
                errorMessages.get(0).code == MALFORMED_JSON_REQUEST_CODE
                errorMessages.get(0).detail == MALFORMED_EMAIL_ADDRESS_DETAILS
            }
    }

    def "get an error when attempt to register with an invalid password"() {
        given: "the data of a new user with an invalid password"
            def userRegistrationRequest = new UserRegistrationRequestDto("john", "doe@mail.com", "password", null)

        when: "when post is performed with the data of the new user"
            def requestEntity = new HttpEntity<>(userRegistrationRequest)
            def responseEntity = restTemplate.postForEntity(URL_SIGNUP, requestEntity, ErrorMessageResponseDto.class)

        then: "the status code is 400"
            responseEntity.statusCode == BAD_REQUEST

        and: "the response body contains an error message"
            verifyAll(responseEntity.getBody()) {
                errorMessages.size() == 1
                errorMessages.get(0).getTimestamp() < LocalDateTime.now()
                errorMessages.get(0).code == MALFORMED_JSON_REQUEST_CODE
                errorMessages.get(0).detail == INVALID_PASSWORD_DETAILS
            }
    }

    def "get an error when attempt to register with a used email address"() {
        when: "when post is performed with the data of the new user"
            def requestEntity = new HttpEntity<>(registeredUserRequest)
            def responseEntity = restTemplate.postForEntity(URL_SIGNUP, requestEntity, ErrorMessageResponseDto.class)

        then: "the status code is 400"
            responseEntity.statusCode == CONFLICT

        and: "the response body contains an error message"
            verifyAll(responseEntity.getBody()) {
                errorMessages.size() == 1
                errorMessages.get(0).getTimestamp() < LocalDateTime.now()
                errorMessages.get(0).code == EMAIL_ALREADY_REGISTERED_CODE
                errorMessages.get(0).detail == EMAIL_ALREADY_REGISTERED_DETAILS
            }
    }

}
