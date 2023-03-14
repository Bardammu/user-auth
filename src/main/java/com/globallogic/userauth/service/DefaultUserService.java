package com.globallogic.userauth.service;

import com.globallogic.userauth.model.Phone;
import com.globallogic.userauth.model.User;
import com.globallogic.userauth.model.request.UserRegistrationRequest;
import com.globallogic.userauth.model.response.UserRegistrationResponse;
import com.globallogic.userauth.repository.UserRepository;
import com.globallogic.userauth.security.JwtTokenManager;
import com.globallogic.userauth.validation.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

/**
 * Default implementation of {@link UserService}
 *
 * @since 1.0.0
 */
@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenManager jwtTokenManager;

    public DefaultUserService(@Autowired UserRepository userRepository,
                              @Autowired PasswordEncoder passwordEncoder,
                              @Autowired JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public UserRegistrationResponse registerNewUser(UserRegistrationRequest userRegistrationRequest) {
        if (userRepository.existsByEmail(userRegistrationRequest.getEmail())) {
            throw new UserAlreadyExistException(format("The email '%s' is already used", userRegistrationRequest.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());

        User user = new User();
        user.setName(userRegistrationRequest.getName());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setPhones(userRegistrationRequest.getPhones());
        user.setPassword(encodedPassword);
        user.setActive(true);

        user = userRepository.saveAndFlush(user);

        UUID id = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        List<Phone> phones = user.getPhones();
        LocalDateTime created = user.getCreated();
        boolean isActive = user.isActive();

        String token = jwtTokenManager.generateJwtToken(user.getEmail());

        return new UserRegistrationResponse(id, name, email, phones, created, user.getLastLogin(), token, isActive);
    }

}
