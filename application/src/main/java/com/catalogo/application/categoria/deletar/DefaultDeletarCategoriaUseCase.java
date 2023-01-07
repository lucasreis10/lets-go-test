package com.catalogo.application.categoria.deletar;

import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.categoria.CategoriaID;

import java.util.Objects;

public class DefaultDeletarCategoriaUseCase extends DeletarCategoriaUseCase {

    private CategoriaGateway categoriaGateway;


    public DefaultDeletarCategoriaUseCase(final CategoriaGateway categoriaGateway) {
        this.categoriaGateway = Objects.requireNonNull(categoriaGateway);
    }

    @Override
    public void execute(String command) {
        CategoriaID categoriaID = CategoriaID.from(command);

        categoriaGateway.deletarPorId(categoriaID);

    }
}
