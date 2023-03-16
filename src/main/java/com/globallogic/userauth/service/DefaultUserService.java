package com.globallogic.userauth.service;

import com.globallogic.userauth.dto.PhoneDto;
import com.globallogic.userauth.dto.UserRegistrationRequestDto;
import com.globallogic.userauth.dto.UserRegistrationResponseDto;
import com.globallogic.userauth.exception.UserAlreadyExistException;
import com.globallogic.userauth.model.Phone;
import com.globallogic.userauth.model.User;
import com.globallogic.userauth.repository.PhoneRepository;
import com.globallogic.userauth.repository.UserRepository;
import com.globallogic.userauth.security.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    private final PhoneRepository phoneRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenManager jwtTokenManager;

    public DefaultUserService(@Autowired UserRepository userRepository,
                              @Autowired PhoneRepository phoneRepository,
                              @Autowired PasswordEncoder passwordEncoder,
                              @Autowired JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public UserRegistrationResponseDto registerNewUser(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new UserAlreadyExistException(format("The email '%s' is already used", userRegistrationRequestDto.getEmail()));
        }

        String name = userRegistrationRequestDto.getName();
        String email = userRegistrationRequestDto.getEmail();
        List<Phone> phones = userRegistrationRequestDto.getPhones().stream().map(p -> {
            Phone phone = new Phone();
            phone.setNumber(p.getNumber());
            phone.setCityCode(p.getCityCode());
            phone.setCountryCode(p.getCountryCode());
            return phone;
        }).collect(toList());
        String encodedPassword = passwordEncoder.encode(userRegistrationRequestDto.getPassword());
        boolean isActive = true;

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhones(phones);
        user.setActive(isActive);
        user.setPassword(encodedPassword);

        user = userRepository.saveAndFlush(user);

        UUID id = user.getId();
        List<PhoneDto> phoneDtos = userRegistrationRequestDto.getPhones();
        LocalDateTime created = user.getCreated();
        LocalDateTime lastLogin = user.getLastLogin();
        String token = jwtTokenManager.generateJwtToken(user.getEmail());

        return new UserRegistrationResponseDto(id, name, email, phoneDtos, created, lastLogin, token, isActive);
    }

    @Override
    public UserRegistrationResponseDto getUser(String email) {
        Optional<User> userOptional =  userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(format("User with email '%s' not found", email));
        }

        User user = userOptional.get();

        UUID id = user.getId();
        String name = user.getName();
        List<PhoneDto> phoneDtos = user.getPhones().stream()
                .map(p -> new PhoneDto(p.getNumber(), p.getCityCode(), p.getCountryCode())).collect(toList());
        LocalDateTime created = user.getCreated();
        LocalDateTime lastLogin = user.getLastLogin();
        boolean isActive = user.isActive();
        String token = jwtTokenManager.generateJwtToken(user.getEmail());

        return new UserRegistrationResponseDto(id, name, email, phoneDtos, created, lastLogin, token, isActive);
    }

}
