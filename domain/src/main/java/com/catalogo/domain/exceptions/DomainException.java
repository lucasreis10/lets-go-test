package com.catalogo.domain.exceptions;

import com.catalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends RuntimeException{

    private final List<Error> erros;

    DomainException(final String message, List<Error> erros) {
        super(message, null, true, false);
        this.erros = erros;
    }

    public static DomainException with(final List<Error> erros) {
        return new DomainException("", erros);
    }

    public static DomainException with(Error error) {
        return new DomainException(error.getMessage(), List.of(error));
    }


    public List<Error> getErros() {
        return erros;
    }
}
