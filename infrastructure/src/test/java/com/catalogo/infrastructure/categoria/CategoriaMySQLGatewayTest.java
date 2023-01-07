package com.catalogo.infrastructure.categoria;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaID;
import com.catalogo.domain.pagination.SearchQuery;
import com.catalogo.MySQLGatewayTest;
import com.catalogo.infrastructure.categoria.persistence.CategoriaJpaEntity;
import com.catalogo.infrastructure.categoria.persistence.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoriaMySQLGatewayTest {

    @Autowired
    private CategoriaMySQLGateway categoriaGateway;

    @Autowired
    private CategoriaRepository categoriaRepository;


    @Test
    public void dadoUmaCategoriaValida_quandoExecutarCriarCategoria_entaoUmaNovaCategoriaRetorna() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        assertEquals(0, categoriaRepository.count());

        final var novaCategoria = categoriaGateway.criar(categoria);

        assertEquals(1, categoriaRepository.count());

        assertEquals(categoria.getId(), novaCategoria.getId());
        assertEquals(nomeEsperado, novaCategoria.getNome());
        assertEquals(descricaoEsperada, novaCategoria.getDescricao());
        assertEquals(isAtivoEsperado, novaCategoria.isAtivo());
        assertEquals(categoria.getDataCriacao(), novaCategoria.getDataCriacao());
        assertEquals(categoria.getDataAtualizacao(), novaCategoria.getDataAtualizacao());
        assertEquals(categoria.getDataDelecao(), novaCategoria.getDataDelecao());
        assertNull(novaCategoria.getDataDelecao());

        final var categoriaEntity = categoriaRepository.findById(categoria.getId().getValue()).get();


        assertEquals(categoria.getId().getValue(), categoriaEntity.getId());
        assertEquals(nomeEsperado, categoriaEntity.getNome());
        assertEquals(descricaoEsperada, categoriaEntity.getDescricao());
        assertEquals(isAtivoEsperado, categoriaEntity.isAtivo());
        assertEquals(categoria.getDataCriacao(), categoriaEntity.getDataCriacao());
        assertEquals(categoria.getDataAtualizacao(), categoriaEntity.getDataAtualizacao());
        assertEquals(categoria.getDataDelecao(), categoriaEntity.getDataDelecao());
        assertNull(categoriaEntity.getDataDelecao());
    }

    @Test
    public void dadoUmaCategoriaValida_quandoExecutarAtualizarCategoria_entaoCategoriaAtualizadaRetorna() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;

        final var categoria = Categoria.newCategoria("Serie", null, true);

        assertEquals(0, categoriaRepository.count());

        categoriaRepository.saveAndFlush(CategoriaJpaEntity.from(categoria));

        assertEquals(1, categoriaRepository.count());

        final var novaCategoria = categoria.clone()
                .atualizar(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        final var categoriaAtualizada = categoriaGateway.atualizar(novaCategoria);

        assertEquals(1, categoriaRepository.count());

        assertEquals(categoria.getId(), categoriaAtualizada.getId());
        assertEquals(nomeEsperado, categoriaAtualizada.getNome());
        assertEquals(descricaoEsperada, categoriaAtualizada.getDescricao());
        assertEquals(isAtivoEsperado, categoriaAtualizada.isAtivo());
        assertEquals(categoria.getDataCriacao(), categoriaAtualizada.getDataCriacao());
        assertTrue(categoria.getDataAtualizacao().isBefore(categoriaAtualizada.getDataAtualizacao()));
        assertEquals(categoria.getDataDelecao(), categoriaAtualizada.getDataDelecao());
        assertNull(categoriaAtualizada.getDataDelecao());

        final var categoriaEntity = categoriaRepository.findById(categoria.getId().getValue()).get();

        assertEquals(categoria.getId().getValue(), categoriaEntity.getId());
        assertEquals(nomeEsperado, categoriaEntity.getNome());
        assertEquals(descricaoEsperada, categoriaEntity.getDescricao());
        assertEquals(isAtivoEsperado, categoriaEntity.isAtivo());
        assertEquals(categoria.getDataCriacao(), categoriaEntity.getDataCriacao());
        assertTrue(categoria.getDataAtualizacao().isBefore(categoriaEntity.getDataAtualizacao()));
        assertEquals(categoria.getDataDelecao(), categoriaEntity.getDataDelecao());
        assertNull(categoriaEntity.getDataDelecao());
    }

    @Test
    public void dadoUmaIdCategoriaPrePersistidaValida_quandoTentadoChamarDelecao_entaoCategoriaDeletada() {
        Categoria categoria = Categoria.newCategoria("dummy", null, true);

        assertEquals(0, categoriaRepository.count());

        categoriaRepository.saveAndFlush(CategoriaJpaEntity.from(categoria));

        assertEquals(1, categoriaRepository.count());

        categoriaGateway.deletarPorId(categoria.getId());

        assertEquals(0, categoriaRepository.count());
    }

      @Test
    public void dadoUmaIdCategoriaPrePersistidaInvalida_quandoTentadoChamarDelecao_entaoCategoriaDeletada() {
          assertEquals(0, categoriaRepository.count());

          categoriaGateway.deletarPorId(CategoriaID.from("invalido"));

          assertEquals(0, categoriaRepository.count());
    }

    @Test
    public void dadoUmaIdCategoriaPrePersistidaValida_quandoExecutarObterCategoriaPorId_entaoCategoriaRetorna() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        assertEquals(0, categoriaRepository.count());

        categoriaRepository.saveAndFlush(CategoriaJpaEntity.from(categoria));

        assertEquals(1, categoriaRepository.count());

        final var categoriaRecuperada = categoriaGateway.obterPorId(categoria.getId()).get();

        assertEquals(categoria.getId(), categoriaRecuperada.getId());
        assertEquals(nomeEsperado, categoriaRecuperada.getNome());
        assertEquals(descricaoEsperada, categoriaRecuperada.getDescricao());
        assertEquals(isAtivoEsperado, categoriaRecuperada.isAtivo());
        assertEquals(categoria.getDataCriacao(), categoriaRecuperada.getDataCriacao());
        assertEquals(categoria.getDataAtualizacao(), categoriaRecuperada.getDataAtualizacao());
        assertEquals(categoria.getDataDelecao(), categoriaRecuperada.getDataDelecao());
        assertNull(categoriaRecuperada.getDataDelecao());
    }

    @Test
    public void dadoUmaIdCategoriaNaoArmazenadaValida_quandoExecutarObterCategoriaPorId_entaoRetornaEmpty() {
        assertEquals(0, categoriaRepository.count());

        final var categoriaRecuperada = categoriaGateway.obterPorId(CategoriaID.from("empty"));

        assertTrue(categoriaRecuperada.isEmpty());
    }

    @Test
    public void dadoCategoriasPrePersistidas_quandoChamadoObterTodas_entaoRetornaPagination() {
        final var pageEsperada = 0;
        final var perPageEsperada = 1;
        final var totalEsperada = 3;

        final var filmes = Categoria.newCategoria("filmes", null, true);
        final var series = Categoria.newCategoria("series", null, true);
        final var documentarios = Categoria.newCategoria("documentarios", null, true);

        assertEquals(0, categoriaRepository.count());

        categoriaRepository.saveAll(List.of(
                CategoriaJpaEntity.from(filmes),
                CategoriaJpaEntity.from(series),
                CategoriaJpaEntity.from(documentarios)
        ));

        assertEquals(3, categoriaRepository.count());

        final var query = new SearchQuery(0, 1, "", "nome", "asc");
        final var result = categoriaGateway.obterTodos(query);

        assertEquals(pageEsperada, result.getCurrentPage());
        assertEquals(perPageEsperada, result.getPerPage());
        assertEquals(totalEsperada, result.getTotal());
        assertEquals(perPageEsperada, result.getItems().size());
        assertEquals(documentarios.getId(), result.getItems().get(0).getId());
    }

    @Test
    public void dadoTableCategoriaVazia_quandoChamadoObterTodas_entaoRetornaPaginationEmpty() {
        var pageEsperada = 0;
        final var perPageEsperada = 1;
        final var totalEsperada = 0;

        assertEquals(0, categoriaRepository.count());

        final var query = new SearchQuery(0, 1, "", "nome", "asc");
        final var result = categoriaGateway.obterTodos(query);

        assertEquals(pageEsperada, result.getCurrentPage());
        assertEquals(perPageEsperada, result.getPerPage());
        assertEquals(totalEsperada, result.getTotal());
        assertEquals(0, result.getItems().size());
    }

    @Test
    public void dadoUmPagination_quandoChamadoObterTodasComPage1_entaoRetornaPagination() {
        var pageEsperada = 0;
        final var perPageEsperada = 1;
        final var totalEsperada = 3;

        final var filmes = Categoria.newCategoria("filmes", null, true);
        final var series = Categoria.newCategoria("series", null, true);
        final var documentarios = Categoria.newCategoria("documentarios", null, true);

        assertEquals(0, categoriaRepository.count());

        categoriaRepository.saveAll(List.of(
                CategoriaJpaEntity.from(filmes),
                CategoriaJpaEntity.from(series),
                CategoriaJpaEntity.from(documentarios)
        ));

        assertEquals(3, categoriaRepository.count());

        var query = new SearchQuery(0, 1, "", "nome", "asc");
        var result = categoriaGateway.obterTodos(query);

        assertEquals(pageEsperada, result.getCurrentPage());
        assertEquals(perPageEsperada, result.getPerPage());
        assertEquals(totalEsperada, result.getTotal());
        assertEquals(perPageEsperada, result.getItems().size());
        assertEquals(documentarios.getId(), result.getItems().get(0).getId());

        // Page 1
        pageEsperada = 1;

         query = new SearchQuery(1, 1, "", "nome", "asc");
         result = categoriaGateway.obterTodos(query);

        assertEquals(pageEsperada, result.getCurrentPage());
        assertEquals(perPageEsperada, result.getPerPage());
        assertEquals(totalEsperada, result.getTotal());
        assertEquals(perPageEsperada, result.getItems().size());
        assertEquals(filmes.getId(), result.getItems().get(0).getId());

        // Page 2
        pageEsperada = 2;

        query = new SearchQuery(2, 1, "", "nome", "asc");
        result = categoriaGateway.obterTodos(query);

        assertEquals(pageEsperada, result.getCurrentPage());
        assertEquals(perPageEsperada, result.getPerPage());
        assertEquals(totalEsperada, result.getTotal());
        assertEquals(perPageEsperada, result.getItems().size());
        assertEquals(series.getId(), result.getItems().get(0).getId());
    }

    @Test
    public void dadoCategoriasPrePersistidasEUsarTermDoc_quandoChamadoObterTodas_entaoRetornaPagination() {
        final var pageEsperada = 0;
        final var perPageEsperada = 1;
        final var totalEsperada = 1;

        final var filmes = Categoria.newCategoria("filmes", null, true);
        final var series = Categoria.newCategoria("series", null, true);
        final var documentarios = Categoria.newCategoria("documentarios", null, true);

        assertEquals(0, categoriaRepository.count());

        categoriaRepository.saveAll(List.of(
                CategoriaJpaEntity.from(filmes),
                CategoriaJpaEntity.from(series),
                CategoriaJpaEntity.from(documentarios)
        ));

        assertEquals(3, categoriaRepository.count());

        final var query = new SearchQuery(0, 1, "doc", "nome", "asc");
        final var result = categoriaGateway.obterTodos(query);

        assertEquals(pageEsperada, result.getCurrentPage());
        assertEquals(perPageEsperada, result.getPerPage());
        assertEquals(totalEsperada, result.getTotal());
        assertEquals(perPageEsperada, result.getItems().size());
        assertEquals(documentarios.getId(), result.getItems().get(0).getId());
    }

    @Test
    public void dadoCategoriasPrePersistidasEUsarTermAFilmesOriginais_quandoChamadoObterTodas_entaoRetornaPagination() {
        final var pageEsperada = 0;
        final var perPageEsperada = 1;
        final var totalEsperada = 1;

        final var filmes = Categoria.newCategoria("filmes", "Filmes originais", true);
        final var series = Categoria.newCategoria("series", "Series originais", true);
        final var documentarios = Categoria.newCategoria("documentarios", "Documentarios originais", true);

        assertEquals(0, categoriaRepository.count());

        categoriaRepository.saveAll(List.of(
                CategoriaJpaEntity.from(filmes),
                CategoriaJpaEntity.from(series),
                CategoriaJpaEntity.from(documentarios)
        ));

        assertEquals(3, categoriaRepository.count());

        final var query = new SearchQuery(0, 1, "FILMES ORIGINAIS", "nome", "asc");
        final var result = categoriaGateway.obterTodos(query);

        assertEquals(pageEsperada, result.getCurrentPage());
        assertEquals(perPageEsperada, result.getPerPage());
        assertEquals(totalEsperada, result.getTotal());
        assertEquals(perPageEsperada, result.getItems().size());
        assertEquals(filmes.getId(), result.getItems().get(0).getId());
    }
}
