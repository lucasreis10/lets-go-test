package com.catalogo.domain.validation.handler;

import com.catalogo.domain.exceptions.DomainException;
import com.catalogo.domain.validation.Error;
import com.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> erros;

    public Notification(final List<Error> erros) {
        this.erros = erros;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    public static Notification create(final Throwable throwable) {
        return create(new Error(throwable.getMessage()));
    }

    @Override
    public Notification append(final Error erro) {
        this.erros.add(erro);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        this.erros.addAll(handler.getErrors());
        return this;
    }

    @Override
    public Notification validate(final Validation validation) {
        try {
            validation.validate();
        } catch (DomainException ex) {
            this.erros.addAll(ex.getErros());
        } catch (Throwable  throwable) {
            this.erros.add(new Error(throwable.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.erros;
    }
}
