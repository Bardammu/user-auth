package com.globallogic.userauth.service

import com.globallogic.userauth.model.User
import com.globallogic.userauth.dto.PhoneDto
import com.globallogic.userauth.dto.UserRegistrationRequestDto
import com.globallogic.userauth.dto.UserRegistrationResponseDto
import com.globallogic.userauth.repository.UserRepository
import com.globallogic.userauth.security.JwtTokenManager
import com.globallogic.userauth.validation.UserAlreadyExistException
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime

class DefaultUserServiceSpec extends Specification {

    private UserService userService

    private PasswordEncoder passwordEncoder

    private JwtTokenManager jwtTokenManager;

    private UserRepository userRepository

    @Shared
    private UserRegistrationRequestDto userRegistrationRequestCompletedData

    @Shared
    private UserRegistrationRequestDto userRegistrationRequestMissingName

    @Shared
    private UserRegistrationRequestDto userRegistrationRequestNoPhones

    def setupSpec() {
        PhoneDto phoneRegistrationRequest = new PhoneDto(12345678L, 11, "+54")
        userRegistrationRequestCompletedData = new UserRegistrationRequestDto("John", "thejohn@gmail.com", "mysecretpassword", [phoneRegistrationRequest ])
        userRegistrationRequestMissingName = new UserRegistrationRequestDto(null, "thejohn@gmail.com", "mysecretpassword", [phoneRegistrationRequest ])
        userRegistrationRequestNoPhones = new UserRegistrationRequestDto("John", "thejohn@gmail.com", "mysecretpassword", null)
    }

    def setup() {
        passwordEncoder =  Stub()
        jwtTokenManager = Stub()
        userRepository = Stub()
        userService = new DefaultUserService(userRepository, passwordEncoder, jwtTokenManager)
    }

    def "On user registration, the user is saved on the database"() {
        given: "a user repository"
            UUID uuid = UUID.randomUUID()
            LocalDateTime localDateTime = LocalDateTime.now()
            userRepository.saveAndFlush(_ as User) >> { User user ->
                user.setId(uuid)
                user.setCreated(localDateTime)
                user
            }

        when: "the user is register using the UserService"
            UserRegistrationResponseDto userRegistrationResponse = userService.registerNewUser(userRegistrationRequest)

        then: "the user is saved on the database"
            verifyAll(userRegistrationResponse) {
                name ==  userRegistrationRequest.getName()
                email == userRegistrationRequest.getEmail()
                phones == userRegistrationRequest.getPhones()
            }

        where:
            userRegistrationRequest << [userRegistrationRequestCompletedData,
                                        userRegistrationRequestMissingName,
                                        userRegistrationRequestNoPhones]
    }

    def "On user registration, the user gets an UUID"() {
        given: "a user repository that assigns UUIDs to users"
            UUID uuid = UUID.randomUUID()
            userRepository.saveAndFlush(_ as User) >> { User user ->
                user.setId(uuid)
                user
            }

        when: "the user is register using the UserService"
            UserRegistrationResponseDto userRegistrationResponse = userService.registerNewUser(userRegistrationRequestCompletedData)

        then: "the user is saved on the database with an UUID"
            userRegistrationResponse.getId() == uuid
    }

    def "On user registration, the user password is encoded"() {
        given: "a password encoder"
            passwordEncoder.encode(_ as CharSequence) >> "encodedPassword"

        when: "the user is register using the UserService"
            userService.registerNewUser(userRegistrationRequestCompletedData)

        then: "the user is register is store with the encoded password"
           userRepository.saveAndFlush(_ as User) >> { User user ->
               user.getPassword() == "encodedPassword"
               user
            }
    }

    def "On user registration, the user is set to active"() {
        given: "a user repository"
            userRepository.saveAndFlush(_ as User) >> { User user -> user }

        when: "the user is register using the UserService"
            UserRegistrationResponseDto userRegistrationResponse = userService.registerNewUser(userRegistrationRequestCompletedData)

        then: "the user is saved as active"
            userRegistrationResponse.isActive()
    }

    def "On user registration, an authentication token is returned "() {
        given: "a user repository"
            userRepository.saveAndFlush(_ as User) >> { User user -> user }

        and: "a jwt token generator"
            jwtTokenManager.generateJwtToken(_) >> { "token" }

        when: "the user is register using the UserService"
            UserRegistrationResponseDto userRegistrationResponse = userService.registerNewUser(userRegistrationRequestCompletedData)

        then: "a authentication token is returned"
            userRegistrationResponse.getToken() == "token"
    }

    def "On user registration, the creation date is saved"() {
        given: "expected data is setup"
            LocalDateTime localDateTime = LocalDateTime.now()

        and: "a user repository that set the user creation date"
            userRepository.saveAndFlush(_ as User) >> { User user ->
                user.setCreated(localDateTime)
                user
            }

        when: "the user is register using the UserService"
            UserRegistrationResponseDto userRegistrationResponse = userService.registerNewUser(userRegistrationRequestCompletedData)

        then: "the user creation date is returned"
            userRegistrationResponse.getCreated() == localDateTime
    }

    def "On user registration, the last login is saved"() {
        given: "expected data is setup"
            LocalDateTime localDateTime = LocalDateTime.now()

        and: "a user repository that set the user last login"
            userRepository.saveAndFlush(_ as User) >> { User user ->
                user.setLastLogin(localDateTime)
                user
            }

        when: "the user is register using the UserService"
            UserRegistrationResponseDto userRegistrationResponse = userService.registerNewUser(userRegistrationRequestCompletedData)

        then: "the user last login is returned"
            userRegistrationResponse.getLastLogin() == localDateTime
    }

    def "On user registration with taken email, should get an user already exists  error"() {
        given: "a user registration request with a taken email"
            userRepository.existsByEmail(_ as String) >> true

        when: "the user is tried to be register using the UserService"
            userService.registerNewUser(userRegistrationRequestCompletedData)

        then:
            def e = thrown(UserAlreadyExistException)
            e.getMessage() == "The email 'thejohn@gmail.com' is already used"
    }
}
