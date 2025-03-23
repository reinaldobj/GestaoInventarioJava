package com.Acme.GestaoDeInventario.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    //Timestamp do erro
    private LocalDateTime timestamp;

    private String message;

    private int status;

    public ErrorResponse(LocalDateTime timestamp, String message, int status) {
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
