package com.catalogo.application.categoria.atualizar;

import com.catalogo.application.UseCase;
import com.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class AtualizarCategoriaUseCase
        extends UseCase<AtualizarCategoriaCommand, Either<Notification, AtualizarCategoriaOutput>> {
}
