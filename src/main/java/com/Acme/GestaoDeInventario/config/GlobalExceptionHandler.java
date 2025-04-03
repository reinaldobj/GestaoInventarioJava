package com.Acme.GestaoDeInventario.config;

import com.Acme.GestaoDeInventario.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProdutoNaoEncontradoException(ProdutoNaoEncontradoException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.NOT_FOUND.value()  // Código de status HTTP
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNaoEncontradoException(ClienteNaoEncontradoException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.NOT_FOUND.value()  // Código de status HTTP
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ClienteInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleClienteInvalidoException(ClienteInvalidoException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.BAD_REQUEST.value()  // Código de status HTTP
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PedidoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoInvalidoException(PedidoInvalidoException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.BAD_REQUEST.value()  // Código de status HTTP
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ProdutoSemEstoqueException.class)
    public ResponseEntity<ErrorResponse> handleProdutoSemEstoqueException(ProdutoSemEstoqueException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.NOT_FOUND.value()  // Código de status HTTP
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ProdutoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleProdutoInvalidoException(ProdutoInvalidoException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.BAD_REQUEST.value()  // Código de status HTTP
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoNaoEncontradoException(PedidoNaoEncontradoException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.NOT_FOUND.value()  // Código de status HTTP
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UsuarioInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioInvalidoException(UsuarioInvalidoException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.BAD_REQUEST.value()  // Código de status HTTP
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.NOT_FOUND.value()  // Código de status HTTP
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                errors.toString(),  // Mensagem da exceção
                HttpStatus.BAD_REQUEST.value()  // Código de status HTTP
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                errors.toString(),  // Mensagem da exceção
                HttpStatus.BAD_REQUEST.value()  // Código de status HTTP
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),  // Timestamp do erro
                ex.getMessage(),  // Mensagem da exceção
                HttpStatus.BAD_REQUEST.value()  // Código de status HTTP
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }
}