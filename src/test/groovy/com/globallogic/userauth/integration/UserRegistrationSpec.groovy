package com.globallogic.userauth.integration

import com.globallogic.userauth.dto.PhoneDto
import com.globallogic.userauth.dto.UserRegistrationRequestDto
import com.globallogic.userauth.dto.UserRegistrationResponseDto
import org.springframework.http.HttpEntity

import java.time.LocalDateTime

import static org.springframework.http.HttpStatus.CREATED

class UserRegistrationSpec extends IntegrationSpec {


    def "register a new user with valid data"() {
        given: "the data of a new user"
            def phoneRegistrationRequest = new PhoneDto(12341234, 9, "+70")
            def userRegistrationRequest = new UserRegistrationRequestDto("john", "doe@mail.com", "password", [phoneRegistrationRequest ])

        when: "when post is performed with the data of the new user"
            def requestEntity = new HttpEntity<>(userRegistrationRequest);
            def responseEntity = restTemplate.postForEntity(URL_SIGNUP, requestEntity , UserRegistrationResponseDto.class);

        then: "status code is 202"
            responseEntity.statusCode == CREATED

        and: "the response body contains the user data"
            verifyAll(responseEntity.getBody()) {
                name ==  userRegistrationRequest.getName()
                email == userRegistrationRequest.getEmail()
                phones == userRegistrationRequest.getPhones()
                created < LocalDateTime.now()
                lastLogin < LocalDateTime.now()
                jwtTokenManager.getEmailFromToken(token) == userRegistrationRequest.getEmail()
                isActive
            }
    }

}
