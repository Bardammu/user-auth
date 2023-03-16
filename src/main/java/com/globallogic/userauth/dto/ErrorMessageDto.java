package com.globallogic.userauth.dto;

import java.time.LocalDateTime;

/**
 * POJO that represents an error message
 *
 * @since 1.0.0
 */
public class ErrorMessageDto {

    private final LocalDateTime timestamp;

    private final int code;

    private final String detail;

    public ErrorMessageDto(LocalDateTime timestamp, int code, String detail) {
        this.timestamp = timestamp;
        this.code = code;
        this.detail = detail;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }
}
