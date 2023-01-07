package com.catalogo.application.categoria.recuperar.listar;

import com.catalogo.application.UseCase;
import com.catalogo.domain.pagination.SearchQuery;
import com.catalogo.domain.pagination.Pagination;

public abstract class ListarCategoriaUseCase
        extends UseCase<SearchQuery, Pagination<CategoriaListaOutput>> {
}
