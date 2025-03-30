package com.Acme.GestaoDeInventario.exception;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class ErrorResponse {

    //Timestamp do erro
    private final LocalDateTime timestamp;

    //Mensagem de erro
    private final String message;

    //Status do erro
    private final int status;

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
