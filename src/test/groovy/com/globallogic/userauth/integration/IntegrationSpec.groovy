package com.globallogic.userauth.integration

import com.globallogic.userauth.UserAuthApplication
import com.globallogic.userauth.dto.PhoneDto
import com.globallogic.userauth.dto.UserRegistrationRequestDto
import com.globallogic.userauth.security.JwtTokenManager
import org.spockframework.spring.EnableSharedInjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.test.context.jdbc.Sql
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD

@SpringBootTest(classes = UserAuthApplication, webEnvironment = RANDOM_PORT)
@Sql(scripts = "/sql/testData.sql", executionPhase = BEFORE_TEST_METHOD)
@EnableSharedInjection
class IntegrationSpec extends Specification{

    @Autowired
    protected TestRestTemplate restTemplate

    @Autowired
    protected  JwtTokenManager jwtTokenManager

    @LocalServerPort
    @Shared
    private int port

    @Shared
    protected String HOST;

    @Shared
    protected String URL_SIGNUP

    @Shared
    protected String URL_LOGIN

    @Shared
    protected UserRegistrationRequestDto newUserRequest

    @Shared
    protected UserRegistrationRequestDto registeredUserRequest

    def setupSpec() {
        HOST = "http://localhost:" + port
        URL_SIGNUP =  HOST + "/sign-up"
        URL_LOGIN = HOST + "/login"

        def phone = new PhoneDto(12341234, 9, "+70")
        newUserRequest = new UserRegistrationRequestDto("joe", "joedoe@mail.com", "passworD10", [ phone ])

        registeredUserRequest = new UserRegistrationRequestDto("joe", "joe@mail.com", "passworD10", null)
    }

    protected HttpHeaders authenticationHeader(String token) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(APPLICATION_JSON)
        headers.set("Authorization", "Bearer " + token);
        return headers
    }
}
