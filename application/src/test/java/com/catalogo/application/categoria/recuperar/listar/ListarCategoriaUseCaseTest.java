package com.catalogo.application.categoria.recuperar.listar;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.exceptions.DomainException;
import com.catalogo.domain.pagination.Pagination;
import com.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListarCategoriaUseCaseTest {

    @InjectMocks
    private DefaultListarCategoriaUseCase useCase;

    @Mock
    private CategoriaGateway categoriaGateway;

    @BeforeEach
    void cleanUp(){
        reset(categoriaGateway);
    }

    @Test
    public void dadoUmaQueryValida_quandoExecutarListarCategoria_entaoUmaListaCategoriaEhRetornada() {
        final var categorias = List.of(
                Categoria.newCategoria("dummy A", null, true),
                Categoria.newCategoria("dummy B", null, true)
        );

        final var page = 0;
        final var perPage = 10;
        final var terms = "";
        final var sort = "dataCriacao";
        final var direction = "asc";

        final var query =
                new SearchQuery(page, perPage, terms, sort, direction);

        final var pagination =
                new Pagination<>(page, perPage, categorias.size(), categorias);

        final var qtdItens = 2;
        final var resultadoEsperado =
                pagination.map(CategoriaListaOutput::from);

        when(categoriaGateway.obterTodos(eq(query)))
                .thenReturn(pagination);

        final var resultado = useCase.execute(query);

        assertEquals(qtdItens, resultadoEsperado.getItems().size());
        assertEquals(resultadoEsperado, resultado);
        assertEquals(page, resultadoEsperado.getCurrentPage());
        assertEquals(perPage, resultadoEsperado.getPerPage());
        assertEquals(categorias.size(), resultadoEsperado.getTotal());
    }

    @Test
    public void dadoUmaQueryValida_quandoNaoHaResultado_entaoUmaListaVaziaCategoriaEhRetornada() {
        final var categorias = List.<Categoria>of();

        final var page = 0;
        final var perPage = 10;
        final var terms = "";
        final var sort = "dataCriacao";
        final var direction = "asc";


        final var query =
                new SearchQuery(page, perPage, terms, sort, direction);


        final var pagination =
                new Pagination<>(page, perPage, 0, categorias);

        final var qtdItens = 0;
        final var resultadoEsperado = pagination.map(CategoriaListaOutput::from);

        when(categoriaGateway.obterTodos(eq(query)))
                .thenReturn(pagination);

        final var resultado = useCase.execute(query);

        assertEquals(qtdItens, resultadoEsperado.getItems().size());
        assertEquals(resultadoEsperado, resultado);
        assertEquals(page, resultadoEsperado.getCurrentPage());
        assertEquals(perPage, resultadoEsperado.getPerPage());
        assertEquals(categorias.size(), resultadoEsperado.getTotal());
    }

    @Test
    public void dadoUmCommand_quandoExecutarGatewayLancaExecption_entaoUmaExceptionEhRetornada() {
        final var page = 0;
        final var perPage = 10;
        final var terms = "";
        final var sort = "dataCriacao";
        final var direction = "asc";
        final var msgErroEsperada = "Gateway error";

        final var query =
                new SearchQuery(page, perPage, terms, sort, direction);

        when(categoriaGateway.obterTodos(eq(query)))
                .thenThrow(new IllegalStateException(msgErroEsperada));

        final var exceptionEsperada =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        assertEquals(msgErroEsperada, exceptionEsperada.getMessage());

    }

}
