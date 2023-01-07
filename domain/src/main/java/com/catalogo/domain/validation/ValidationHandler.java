package com.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error erro);
    ValidationHandler append(ValidationHandler handler);
    ValidationHandler validate(Validation validation);

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);
        } else {
            return null;
        }
    }

    List<Error> getErrors();

    public interface Validation {
        void validate();
    }

}
