package com.globallogic.userauth.controller;

import com.globallogic.userauth.dto.UserRegistrationRequestDto;
import com.globallogic.userauth.dto.UserResponseDto;
import com.globallogic.userauth.model.User;
import com.globallogic.userauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * Controller with the API implementation to manage users
 *
 * @since 1.0.0
 */
@RestController
public class UserController {

    private final UserService userService;

    public UserController(@Autowired  UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/sign-up", consumes = { APPLICATION_JSON_VALUE }, produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody final UserRegistrationRequestDto userRegistrationRequestDto) {
        UserResponseDto userResponseDto = userService.registerNewUser(userRegistrationRequestDto);
        return status(CREATED).body(userResponseDto);
    }

    @GetMapping(path = "/login", consumes = { APPLICATION_JSON_VALUE }, produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponseDto> login(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserResponseDto userResponseDto = userService.getUser(user.getEmail());
        return ok().body(userResponseDto);
    }
}
