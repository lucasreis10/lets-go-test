package com.catalogo.application.categoria.criar;

import com.catalogo.domain.categoria.Categoria;

public class CriarCategoriaOutput {

    private String id;

    private CriarCategoriaOutput(String id) {
        this.id = id;
    }

    public static CriarCategoriaOutput from(final String id) {
        return new CriarCategoriaOutput(id);
    }

    public static CriarCategoriaOutput from(final Categoria categoria) {
        return new CriarCategoriaOutput(categoria.getId().getValue());
    }

    public String getId() {
        return id;
    }
}
