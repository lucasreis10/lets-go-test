package com.catalogo.application.categoria.recuperar.obter;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.categoria.CategoriaID;
import com.catalogo.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultObterCategoriaPorIdUseCase extends ObterCategoriaPorIdUseCase{

    private final CategoriaGateway categoriaGateway;


    public DefaultObterCategoriaPorIdUseCase(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = Objects.requireNonNull(categoriaGateway);
    }

    @Override
    public CategoriaOutput execute(String input) {
        final var categoriaID = CategoriaID.from(input);

        return categoriaGateway
                .obterPorId(categoriaID)
                .map(CategoriaOutput::from)
                .orElseThrow(notFound(categoriaID));
    }


    private Supplier<NotFoundException> notFound(CategoriaID categoriaID) {
        return () -> NotFoundException.with(Categoria.class, categoriaID);
    }
}
