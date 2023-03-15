package com.globallogic.userauth.integration

import com.globallogic.userauth.UserAuthApplication
import com.globallogic.userauth.security.JwtTokenManager
import org.spockframework.spring.EnableSharedInjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(classes = UserAuthApplication, webEnvironment = RANDOM_PORT)
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

    def setupSpec() {
        HOST = "http://localhost:" + port
        URL_SIGNUP =  HOST + "/sign-up"
    }
}
