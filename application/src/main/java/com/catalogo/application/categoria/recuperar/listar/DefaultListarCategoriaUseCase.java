package com.catalogo.application.categoria.recuperar.listar;

import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.pagination.SearchQuery;
import com.catalogo.domain.pagination.Pagination;

public class DefaultListarCategoriaUseCase extends ListarCategoriaUseCase{

    private final CategoriaGateway categoriaGateway;

    public DefaultListarCategoriaUseCase(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    @Override
    public Pagination<CategoriaListaOutput> execute(SearchQuery query) {
        return categoriaGateway.obterTodos(query)
                .map(CategoriaListaOutput::from);
    }
}
