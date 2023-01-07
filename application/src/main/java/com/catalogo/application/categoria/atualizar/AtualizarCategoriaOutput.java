package com.catalogo.application.categoria.atualizar;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaID;

public class AtualizarCategoriaOutput {

    private final String id;


    private AtualizarCategoriaOutput(final CategoriaID id) {
        this.id = id.getValue();
    }

    public static AtualizarCategoriaOutput from(final String id) {
        return new AtualizarCategoriaOutput(CategoriaID.from(id));
    }

    public static AtualizarCategoriaOutput from(final Categoria categoria) {
        return new AtualizarCategoriaOutput(categoria.getId());
    }

    public String getId() {
        return id;
    }
}
