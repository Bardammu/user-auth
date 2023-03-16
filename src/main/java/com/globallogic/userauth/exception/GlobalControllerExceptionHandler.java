package com.globallogic.userauth.exception;

import com.globallogic.userauth.dto.ErrorMessageDto;
import com.globallogic.userauth.dto.ErrorMessageResponseDto;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static com.globallogic.userauth.validation.Errors.EMAIL_ALREADY_REGISTERED_CODE;
import static com.globallogic.userauth.validation.Errors.EMAIL_ALREADY_REGISTERED_DETAILS;
import static com.globallogic.userauth.validation.Errors.MALFORMED_JSON_REQUEST_CODE;
import static java.util.Collections.singletonList;
import static org.apache.logging.log4j.LogManager.getLogger;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler  {

    Logger logger = getLogger(GlobalControllerExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Request {} raided{}", ((ServletWebRequest)request).getRequest().getRequestURI(), ex);

        String responseBody;
        if (ex.getFieldErrorCount() > 0) {
            responseBody = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        } else  {
            responseBody = ex.getBindingResult().getGlobalErrors().get(0).getDefaultMessage();
        }

        ErrorMessageDto errorMessageDto = new ErrorMessageDto(LocalDateTime.now(), MALFORMED_JSON_REQUEST_CODE, responseBody);
        ErrorMessageResponseDto errorMessageResponseDto = new ErrorMessageResponseDto(singletonList(errorMessageDto));

        return badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(errorMessageResponseDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponseDto> handleUserAlreadyExistsException(UserAlreadyExistException ex, ServletWebRequest request) {
        logger.error("Request {} raided{}", request.getRequest().getRequestURI(), ex);

        ErrorMessageDto errorMessageDto = new ErrorMessageDto(LocalDateTime.now(), EMAIL_ALREADY_REGISTERED_CODE, EMAIL_ALREADY_REGISTERED_DETAILS);

        return status(CONFLICT).body(new ErrorMessageResponseDto(singletonList(errorMessageDto)));
    }

}
