package com.Acme.GestaoDeInventario.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@SuppressWarnings("unused")
public class ErrorResponse {

    private final LocalDateTime timestamp;

    private final String message;

    private final int status;

    public ErrorResponse(LocalDateTime timestamp, String message, int status) {
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
    }
}
