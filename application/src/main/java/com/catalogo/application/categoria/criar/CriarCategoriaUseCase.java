package com.catalogo.application.categoria.criar;

import com.catalogo.application.UseCase;
import com.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CriarCategoriaUseCase
        extends UseCase<CriarCategoriaCommand, Either<Notification, CriarCategoriaOutput>> {


}
