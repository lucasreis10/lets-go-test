package com.catalogo.infrastructure.api.controllers;

import com.catalogo.domain.exceptions.DomainException;
import com.catalogo.domain.exceptions.NotFoundException;
import com.catalogo.domain.validation.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException exception) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(exception));
    }
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleDomainException(final NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(exception));
    }

}

class ApiError {
    private String message;
    private List<Error> errors;


    private ApiError(String message, List<Error> erros) {
        this.message = message;
        this.errors = erros;
    }

    static ApiError from(final DomainException ex) {
        return new ApiError(ex.getMessage(), ex.getErros());
    }

    public String getMessage() {
        return message;
    }

    public List<Error> getErrors() {
        return errors;
    }
}