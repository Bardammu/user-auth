package com.globallogic.userauth.integration

import com.globallogic.userauth.dto.UserResponseDto
import org.springframework.http.HttpEntity

import java.time.LocalDateTime

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

class UserRetrievalSpec extends IntegrationSpec{

    def "register a user and get its information"() {
        given: "a user has been registered and the authentication token saved"
            def requestEntity = new HttpEntity<>(newUserRequest)
            def responseEntity = restTemplate.postForEntity(URL_SIGNUP, requestEntity , UserResponseDto.class)
            def firstToken = responseEntity.getBody().getToken()

        when: "the user information is retrieve with the authentication token"
            requestEntity = new HttpEntity<>(authenticationHeader(firstToken))
            responseEntity = restTemplate.exchange(URL_LOGIN, GET, requestEntity, UserResponseDto.class)

        then: "status code is 200"
            responseEntity.statusCode == OK

        and: "the response body contains the user data"
        verifyAll(responseEntity.getBody()) {
            id != null
            name ==  newUserRequest.getName()
            email == newUserRequest.getEmail()
            phones == newUserRequest.getPhones()
            created < LocalDateTime.now()
            lastLogin < LocalDateTime.now()
            jwtTokenManager.getEmailFromToken(token) == newUserRequest.getEmail()
            active
        }
    }
}
