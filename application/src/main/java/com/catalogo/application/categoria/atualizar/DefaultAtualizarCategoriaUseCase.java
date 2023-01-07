package com.catalogo.application.categoria.atualizar;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.categoria.CategoriaID;
import com.catalogo.domain.exceptions.DomainException;
import com.catalogo.domain.exceptions.NotFoundException;
import com.catalogo.domain.validation.Error;
import com.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultAtualizarCategoriaUseCase extends AtualizarCategoriaUseCase {

    private final CategoriaGateway categoriaGateway;


    public DefaultAtualizarCategoriaUseCase(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = Objects.requireNonNull(categoriaGateway);
    }

    @Override
    public Either<Notification, AtualizarCategoriaOutput> execute(AtualizarCategoriaCommand command) {
        final var categoriaID = CategoriaID.from(command.getId());
        Notification notification = Notification.create();

        final var categoria = categoriaGateway.obterPorId(categoriaID).orElseThrow(notFound(categoriaID));

        categoria
                .atualizar(command.getNome(), command.getDescricao(), command.isAtivo())
                .validate(notification);

        return notification.hasError() ? Left(notification) : update(categoria);
    }

    private Either<Notification, AtualizarCategoriaOutput> update(Categoria categoria) {
        return Try(() -> categoriaGateway.atualizar(categoria))
                .toEither()
                .bimap(Notification::create, AtualizarCategoriaOutput::from);
    }

    private Supplier<NotFoundException> notFound(CategoriaID categoriaID) {
        return () -> NotFoundException.with(Categoria.class, categoriaID);
    }
}
