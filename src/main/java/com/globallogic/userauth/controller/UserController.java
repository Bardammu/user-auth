package com.globallogic.userauth.controller;

import com.globallogic.userauth.dto.UserRegistrationRequestDto;
import com.globallogic.userauth.dto.UserRegistrationResponseDto;
import com.globallogic.userauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
    public ResponseEntity<UserRegistrationResponseDto> signup(@Valid @RequestBody final UserRegistrationRequestDto userRegistrationRequestDto) {
        UserRegistrationResponseDto userRegistrationResponseDto = userService.registerNewUser(userRegistrationRequestDto);
        return status(CREATED).body(userRegistrationResponseDto);
    }
}
