package com.catalogo.domain;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.exceptions.DomainException;
import com.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriaTest {

    @Test
    public void dadoParametrosValido_quandoExecutadoNewCategoria_entaoCategoriaEhInstanciada() {
        final var nomeEsperado = "filmes";
        final var descricaoEsperada = "Categoria mais assistida";
        final var isAtivo = true;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperada, isAtivo);

        assertNotNull(categoria);
        assertNotNull(categoria.getId());
        assertEquals(nomeEsperado, categoria.getNome());
        assertEquals(descricaoEsperada, categoria.getDescricao());
        assertEquals(isAtivo, categoria.isAtivo());
        assertNotNull(categoria.getDataCriacao());
        assertNotNull(categoria.getDataAtualizacao());
        assertNull(categoria.getDataDelecao());
    }

    @Test
    public void dadoUmNomeNullInvalido_quandoExecutadoNewCategoriaEValidate_entaoUmErroDeveSerApresentado() {
        final String nomeEsperado = null;
        final var erroEsperado = "'nome' não pode ser null";
        final var qtdMensagemErro = 1;
        final var descricaoEsperada = "Categoria mais assistida";
        final var isAtivo = true;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperada, isAtivo);
        final var exception = assertThrows(DomainException.class, () -> categoria.validate(new ThrowsValidationHandler()));

        assertEquals(qtdMensagemErro, exception.getErros().size());
        assertEquals(erroEsperado, exception.getErros().get(0).getMessage());
    }

    @Test
    public void dadoUmNomeVazioInvalido_quandoExecutadoNewCategoriaEValidate_entaoUmErroDeveSerApresentado() {
        final var nomeEsperado = "  ";
        final var erroEsperado = "'nome' não pode ser vazio";
        final var qtdMensagemErro = 1;
        final var descricao = "Categoria mais assistida";
        final var isAtivo = true;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricao, isAtivo);
        final var exception = assertThrows(DomainException.class, () -> categoria.validate(new ThrowsValidationHandler()));

        assertEquals(qtdMensagemErro, exception.getErros().size());
        assertEquals(erroEsperado, exception.getErros().get(0).getMessage());
    }

    @Test
    public void dadoUmNomeInvalidoMenorQue3_quandoExecutadoNewCategoriaEValidate_entaoUmErroDeveSerApresentado() {
        final var nomeEsperado = "Du ";
        final var erroEsperado = "'nome' deve ter de 3 a 255 caracteres";
        final var qtdMensagemErro = 1;
        final var descricaoEsperado = "Categoria mais assistida";
        final var isAtivo = true;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperado, isAtivo);
        final var exception = assertThrows(DomainException.class, () -> categoria.validate(new ThrowsValidationHandler()));

        assertEquals(qtdMensagemErro, exception.getErros().size());
        assertEquals(erroEsperado, exception.getErros().get(0).getMessage());
    }

    @Test
    public void dadoUmNomeInvalidoMaiorQue255_quandoExecutadoNewCategoriaEValidate_entaoUmErroDeveSerApresentado() {
        final var nomeEsperado = "Do mesmo modo, a consolidação das estruturas garante a contribuição de um grupo importante na determinação das novas " +
                         "proposições. Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se a valorização de fatores subjetivos faz parte de um processo";
        final var erroEsperado = "'nome' deve ter de 3 a 255 caracteres";
        final var qtdMensagemErro = 1;
        final var descricaoEsperada = "Categoria mais assistida";
        final var isAtivo = true;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperada, isAtivo);
        final var exception = assertThrows(DomainException.class, () -> categoria.validate(new ThrowsValidationHandler()));

        assertEquals(qtdMensagemErro, exception.getErros().size());
        assertEquals(erroEsperado, exception.getErros().get(0).getMessage());
    }

    @Test
    public void dadoUmaDescricaoVaziaValida_quandoExecutadoNewCategoria_entaoCategoriaEhInstanciada() {
        final var nomeEsperado = "filmes";
        final var descricaoEsperada = "  ";
        final var isAtivo = true;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperada, isAtivo);
        assertDoesNotThrow(() -> categoria.validate(new ThrowsValidationHandler()));

        assertNotNull(categoria);
        assertNotNull(categoria.getId());
        assertEquals(nomeEsperado, categoria.getNome());
        assertEquals(descricaoEsperada, categoria.getDescricao());
        assertEquals(isAtivo, categoria.isAtivo());
        assertNotNull(categoria.getDataCriacao());
        assertNotNull(categoria.getDataAtualizacao());
        assertNull(categoria.getDataDelecao());
    }

    @Test
    public void dadoUmAtivoFalseValido_quandoExecutadoNewCategoria_entaoCategoriaEhInstanciada() {
        final var nomeEsperado = "filmes";
        final var descricaoEsperada = "Categoria mais assistida.";
        final var isAtivo = false;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperada, isAtivo);
        assertDoesNotThrow(() -> categoria.validate(new ThrowsValidationHandler()));

        assertNotNull(categoria);
        assertNotNull(categoria.getId());
        assertEquals(nomeEsperado, categoria.getNome());
        assertEquals(descricaoEsperada, categoria.getDescricao());
        assertEquals(isAtivo, categoria.isAtivo());
        assertNotNull(categoria.getDataCriacao());
        assertNotNull(categoria.getDataAtualizacao());
        assertNotNull(categoria.getDataDelecao());
    }

    @Test
    public void dadoUmaCategoriaAtivaValida_quandoExecutadoDesativar_entaoRetornaCategoriaInativa() {
        final var nomeEsperado = "filmes";
        final var descricaoEsperada = "Categoria mais assistida.";
        final var isAtivo = false;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperada, true);
        assertDoesNotThrow(() -> categoria.validate(new ThrowsValidationHandler()));

        final var ataualizacao = categoria.getDataAtualizacao();

        assertTrue(categoria.isAtivo());
        assertNull(categoria.getDataDelecao());

        final var categoriaDesativada = categoria.desativar();

        assertDoesNotThrow(() -> categoria.validate(new ThrowsValidationHandler()));

        assertEquals(categoria.getId(), categoriaDesativada.getId());
        assertEquals(nomeEsperado, categoriaDesativada.getNome());
        assertEquals(descricaoEsperada, categoriaDesativada.getDescricao());
        assertEquals(isAtivo, categoriaDesativada.isAtivo());
        assertNotNull(categoriaDesativada.getDataCriacao());
        assertTrue(categoriaDesativada.getDataAtualizacao().isAfter(ataualizacao));
        assertNotNull(categoriaDesativada.getDataDelecao());
    }

    @Test
    public void dadoUmaCategoriaInativaValida_quandoExecutadoAtivar_entaoRetornaCategoriaAtiva() {
        final var nomeEsperado = "filmes";
        final var descricaoEsperada = "Categoria mais assistida.";
        final var isAtivo = true;

        final var categoria = Categoria.newCategoria(nomeEsperado, descricaoEsperada, false);
        assertDoesNotThrow(() -> categoria.validate(new ThrowsValidationHandler()));

        final var dataAtaualizacao = categoria.getDataAtualizacao();
        final var dataCriacao = categoria.getDataCriacao();

        assertFalse(categoria.isAtivo());
        assertNotNull(categoria.getDataDelecao());

        final var categoriaAtiva = categoria.ativar();

        assertDoesNotThrow(() -> categoria.validate(new ThrowsValidationHandler()));

        assertEquals(categoria.getId(), categoriaAtiva.getId());
        assertEquals(nomeEsperado, categoriaAtiva.getNome());
        assertEquals(descricaoEsperada, categoriaAtiva.getDescricao());
        assertEquals(isAtivo, categoriaAtiva.isAtivo());
        assertNotNull(categoriaAtiva.getDataCriacao());
        assertEquals(categoriaAtiva.getDataCriacao(), dataCriacao);
        assertTrue(categoriaAtiva.getDataAtualizacao().isAfter(dataAtaualizacao));
        assertNull(categoriaAtiva.getDataDelecao());
    }

    @Test
    public void dadoUmaCategoriaValida_quandoExecutadoAtualizar_entaoUmaCategoriaAtualizadaEhRetornada() {
        final var nomeEsperado = "filmes";
        final var descricaoEsperada = "Categoria mais assistida.";
        final var isAtivo = true;

        final var categoria = Categoria.newCategoria("Series", "Series originais", isAtivo);
        assertDoesNotThrow(() -> categoria.validate(new ThrowsValidationHandler()));

        final var dataAtaualizacao = categoria.getDataAtualizacao();
        final var dataCriacao = categoria.getDataCriacao();

        final var categoriaAtualizada =  categoria.atualizar(nomeEsperado, descricaoEsperada, isAtivo);

        assertDoesNotThrow(() -> categoriaAtualizada.validate(new ThrowsValidationHandler()));
        assertEquals(categoria.getId(), categoriaAtualizada.getId());
        assertEquals(nomeEsperado, categoriaAtualizada.getNome());
        assertEquals(descricaoEsperada, categoriaAtualizada.getDescricao());
        assertEquals(isAtivo, categoriaAtualizada.isAtivo());
        assertNotNull(categoriaAtualizada.getDataCriacao());
        assertEquals(categoriaAtualizada.getDataCriacao(), dataCriacao);
        assertTrue(categoriaAtualizada.getDataAtualizacao().isAfter(dataAtaualizacao));
        assertNull(categoriaAtualizada.getDataDelecao());
    }

    @Test
    public void dadoUmaCategoriaValida_quandoExecutadoAtualizarParaInativar_entaoUmaCategoriaAtualizadaEhRetornada() {
        final var nomeEsperado = "filmes";
        final var descricaoEsperada = "Categoria mais assistida.";
        final var isAtivo = false;

        final var categoria = Categoria.newCategoria("Series", "Series originais", true);

        assertDoesNotThrow(() -> categoria.validate(new ThrowsValidationHandler()));
        assertTrue(categoria.isAtivo());
        assertNull(categoria.getDataDelecao());

        final var dataAtaualizacao = categoria.getDataAtualizacao();
        final var dataCriacao = categoria.getDataCriacao();

        final var categoriaAtualizada =  categoria.atualizar(nomeEsperado, descricaoEsperada, isAtivo);

        assertDoesNotThrow(() -> categoriaAtualizada.validate(new ThrowsValidationHandler()));
        assertEquals(categoria.getId(), categoriaAtualizada.getId());
        assertEquals(nomeEsperado, categoriaAtualizada.getNome());
        assertEquals(descricaoEsperada, categoriaAtualizada.getDescricao());
        assertFalse(categoria.isAtivo());
        assertNotNull(categoria.getDataDelecao());
        assertNotNull(categoriaAtualizada.getDataCriacao());
        assertEquals(categoriaAtualizada.getDataCriacao(), dataCriacao);
        assertTrue(categoriaAtualizada.getDataAtualizacao().isAfter(dataAtaualizacao));
    }

    @Test
    public void dadoUmaCategoriaValida_quandoExecutadoAtualizarComParametroInvalido_entaoUmaCategoriaAtualizadaEhRetornada() {
        final String nomeEsperado = null;
        final var descricaoEsperada = "Categoria mais assistida.";
        final var isAtivo = true;

        final var categoria = Categoria.newCategoria("Series", "Series originais", isAtivo);

        assertDoesNotThrow(() -> categoria.validate(new ThrowsValidationHandler()));

        final var dataAtaualizacao = categoria.getDataAtualizacao();
        final var dataCriacao = categoria.getDataCriacao();

        final var categoriaAtualizada =  categoria.atualizar(nomeEsperado, descricaoEsperada, isAtivo);

        assertEquals(categoria.getId(), categoriaAtualizada.getId());
        assertEquals(nomeEsperado, categoriaAtualizada.getNome());
        assertEquals(descricaoEsperada, categoriaAtualizada.getDescricao());
        assertTrue(categoria.isAtivo());
        assertNull(categoria.getDataDelecao());
        assertNotNull(categoriaAtualizada.getDataCriacao());
        assertEquals(categoriaAtualizada.getDataCriacao(), dataCriacao);
        assertTrue(categoriaAtualizada.getDataAtualizacao().isAfter(dataAtaualizacao));
    }
}
