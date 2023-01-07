package com.catalogo.domain.categoria;

import com.catalogo.domain.pagination.SearchQuery;
import com.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoriaGateway {

    Categoria criar(Categoria categoria);

    void deletarPorId(CategoriaID id);

    Optional<Categoria> obterPorId(CategoriaID id);

    Categoria atualizar(Categoria categoria);

    Pagination<Categoria> obterTodos(SearchQuery query);

}
