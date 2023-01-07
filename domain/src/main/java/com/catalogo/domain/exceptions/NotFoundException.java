package com.catalogo.domain.exceptions;

import com.catalogo.domain.AggregateRoot;
import com.catalogo.domain.Identifier;
import com.catalogo.domain.validation.Error;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> aggregate,
            final Identifier id
    ) {
        final var error = String
                .format("%s com ID %s não foi encontrado.",
                        aggregate.getSimpleName(),
                        id.getValue()
                );
        return new NotFoundException(error, Collections.emptyList());
    }

}
