package com.catalogo.infrastructure.categoria.presenters;

import com.catalogo.application.categoria.recuperar.listar.CategoriaListaOutput;
import com.catalogo.infrastructure.categoria.models.ListarCategoriaResponse;

public class CategoriaPresenter {

    public static ListarCategoriaResponse present(final CategoriaListaOutput output) {
        return new ListarCategoriaResponse(
                output.getId().getValue(),
                output.getNome(),
                output.getDescricao(),
                output.isAtivo(),
                output.getDataCriacao(),
                output.getDataDelecao()
        );
    }

}
