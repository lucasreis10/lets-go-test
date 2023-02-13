package com.catalogo.application.categoria.recuperar.listar;

import com.catalogo.IntegrationTest;
import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.pagination.SearchQuery;
import com.catalogo.infrastructure.categoria.persistence.CategoriaJpaEntity;
import com.catalogo.infrastructure.categoria.persistence.CategoriaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class ListarCategoriaUseCaseITTest {

    @Autowired
    private ListarCategoriaUseCase useCase;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    void mockUp() {
        final var categorias = Stream.of(
                        Categoria.newCategoria("Filmes", null, true),
                        Categoria.newCategoria("Netflix Originals", "Títulos de autoria da Netflix", true),
                        Categoria.newCategoria("Amazon Originals", "Títulos de autoria da Amazon Prime", true),
                        Categoria.newCategoria("Documentários", null, true),
                        Categoria.newCategoria("Sports", null, true),
                        Categoria.newCategoria("Kids", "Categoria para crianças", true),
                        Categoria.newCategoria("Series", null, true)
                )
                .map(CategoriaJpaEntity::from).collect(Collectors.toList());

        categoriaRepository.saveAllAndFlush(categorias);
    }

    @Test
    public void dadoUmaQueryValida_quandoExecutarListarCategoriaComTermoSemCorrespondencia_entaoUmaListaCategoriaEhRetornada() {
        final var page = 0;
        final var perPage = 10;
        final var terms = "adseadsead2";
        final var sort = "nome";
        final var direction = "asc";
        final var total = 0;
        final var qtdItens = 0;

        final var query =
                new SearchQuery(page, perPage, terms, sort, direction);

        final var resultado = useCase.execute(query);

        assertEquals(qtdItens, resultado.getItems().size());
        assertEquals(page, resultado.getCurrentPage());
        assertEquals(perPage, resultado.getPerPage());
        assertEquals(total, resultado.getTotal());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "crianças,0,10,1,1,Kids",
            "da Amazon,0,10,1,1,Amazon Originals",
    })
    public void dadoUmaQueryValida_quandoExecutarListarCategoria_entaoCategoriasOrdenadasEhRetornada(
            final String terms,
            final int page,
            final int perPage,
            final int itemsCount,
            final long total,
            final String nomeCategoria
    ) {
        final var sort = "nome";
        final var direction = "asc";

        final var query =
                new SearchQuery(page, perPage, terms, sort, direction);

        final var output = useCase.execute(query);

        Assertions.assertEquals(itemsCount, output.getItems().size());
        Assertions.assertEquals(page, output.getCurrentPage());
        Assertions.assertEquals(perPage, output.getPerPage());
        Assertions.assertEquals(total, output.getTotal());
        Assertions.assertEquals(nomeCategoria, output.getItems().get(0).getNome());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Documentários",
            "1,2,2,7,Filmes;Kids",
            "2,2,2,7,Netflix Originals;Series",
            "3,2,1,7,Sports",
    })
    public void dadoUmaQueryValida_quandoExecutarListarCategoria_entaoCategoriasPaginadaEhRetornada(
            final int page,
            final int perPage,
            final int itemsCount,
            final long total,
            final String nomesCategoria
    ) {
        final var sort = "nome";
        final var direction = "asc";
        final var terms = "";

        final var query =
                new SearchQuery(page, perPage, terms, sort, direction);

        final var output = useCase.execute(query);

        Assertions.assertEquals(itemsCount, output.getItems().size());
        Assertions.assertEquals(page, output.getCurrentPage());
        Assertions.assertEquals(perPage, output.getPerPage());
        Assertions.assertEquals(total, output.getTotal());

        int index = 0;
        for (final String expectedName : nomesCategoria.split(";")) {
            final String actualName = output.getItems().get(index).getNome();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }
}
