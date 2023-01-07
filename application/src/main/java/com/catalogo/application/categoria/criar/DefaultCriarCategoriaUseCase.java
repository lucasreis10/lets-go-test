package com.catalogo.application.categoria.criar;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.validation.handler.Notification;
import com.catalogo.domain.validation.handler.ThrowsValidationHandler;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.*;

public class DefaultCriarCategoriaUseCase extends CriarCategoriaUseCase{

    private final CategoriaGateway categoriaGateway;

    public DefaultCriarCategoriaUseCase(final CategoriaGateway categoriaGateway) {
        this.categoriaGateway = Objects.requireNonNull(categoriaGateway);
    }

    @Override
    public Either<Notification, CriarCategoriaOutput> execute(final CriarCategoriaCommand command) {
        var categoria = Categoria.newCategoria(command.getNome(), command.getDescricao(), command.isAtivo());

        final var notification = Notification.create();
        categoria.validate(notification);

        return notification.hasError() ? Left(notification) : create(categoria);
    }

    private Either<Notification, CriarCategoriaOutput> create(Categoria categoria) {
        return Try(() -> categoriaGateway.criar(categoria))
                .toEither()
                .bimap(Notification::create, CriarCategoriaOutput::from);

    }
}
