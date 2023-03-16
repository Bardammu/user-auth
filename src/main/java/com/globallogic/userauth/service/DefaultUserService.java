package com.globallogic.userauth.service;

import com.globallogic.userauth.dto.PhoneDto;
import com.globallogic.userauth.dto.UserRegistrationRequestDto;
import com.globallogic.userauth.dto.UserResponseDto;
import com.globallogic.userauth.exception.UserAlreadyExistException;
import com.globallogic.userauth.model.Phone;
import com.globallogic.userauth.model.User;
import com.globallogic.userauth.repository.UserRepository;
import com.globallogic.userauth.security.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Default implementation of {@link UserService}
 *
 * @since 1.0.0
 */
@Service
@Transactional
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
    public UserResponseDto registerNewUser(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new UserAlreadyExistException(format("The email '%s' is already used", userRegistrationRequestDto.getEmail()));
        }

        User user = getUser(userRegistrationRequestDto);

        user = userRepository.saveAndFlush(user);

        List<PhoneDto> phoneDtos = userRegistrationRequestDto.getPhones();
        String token = jwtTokenManager.generateJwtToken(user.getEmail());

        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), phoneDtos,
                user.getCreated(), user.getLastLogin(), token, user.isActive());
    }

    @Override
    public UserResponseDto getUser(String email) {
        Optional<User> userOptional =  userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(format("User with email '%s' not found", email));
        }

        User user = userOptional.get();

        List<PhoneDto> phoneDtos = user.getPhones().stream()
                .map(p -> new PhoneDto(p.getNumber(), p.getCityCode(), p.getCountryCode())).collect(toList());
        String token = jwtTokenManager.generateJwtToken(user.getEmail());

        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), phoneDtos,
                user.getCreated(), user.getLastLogin(), token, user.isActive());
    }

    private User getUser(UserRegistrationRequestDto userRegistrationRequestDto) {
        User user = new User();

        user.setName(userRegistrationRequestDto.getName());
        user.setEmail(userRegistrationRequestDto.getEmail());

        List<Phone> phones = userRegistrationRequestDto.getPhones().stream().map(p -> {
            Phone phone = new Phone();
            phone.setNumber(p.getNumber());
            phone.setCityCode(p.getCityCode());
            phone.setCountryCode(p.getCountryCode());
            return phone;
        }).collect(toList());
        user.setPhones(phones);

        String encodedPassword = passwordEncoder.encode(userRegistrationRequestDto.getPassword());
        user.setPassword(encodedPassword);

        user.setActive(true);

        return user;
    }
}
