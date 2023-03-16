package com.globallogic.userauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * POJO that represents an error message response
 *
 * @since 1.0.0
 */
public class ErrorMessageResponseDto {

    @JsonProperty(value="error")
    private final List<ErrorMessageDto> errorMessages;

    public ErrorMessageResponseDto(@JsonProperty(value="error") List<ErrorMessageDto> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<ErrorMessageDto> getErrorMessages() {
        return errorMessages;
    }
}
