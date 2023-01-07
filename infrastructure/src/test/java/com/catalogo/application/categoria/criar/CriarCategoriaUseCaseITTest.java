package com.catalogo.application.categoria.criar;

import com.catalogo.IntegrationTest;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.infrastructure.categoria.persistence.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class CriarCategoriaUseCaseITTest {

    @Autowired
    private CriarCategoriaUseCase useCase;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @SpyBean
    private CategoriaGateway categoriaGateway;

    @Test
    public void dadoUmCommandValido_quandoExecutarCriarCategoria_deveRetornaUmaCategoriaComId() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var command = CriarCategoriaCommand.with(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        assertEquals(0, categoriaRepository.count());

        final var output = useCase.execute(command).get();
        final var idEsperado = output.getId();

        assertEquals(1, categoriaRepository.count());
        assertNotNull(output);
        assertNotNull(output.getId());

        final var categoria = categoriaRepository.findById(idEsperado).get();

        assertEquals(idEsperado, categoria.getId());
        assertEquals(nomeEsperado, categoria.getNome());
        assertEquals(descricaoEsperada, categoria.getDescricao());
        assertEquals(isAtivoEsperado, categoria.isAtivo());
        assertNotNull(categoria.getDataCriacao());
        assertNotNull(categoria.getDataAtualizacao());
        assertNull(categoria.getDataDelecao());
    }

    @Test
    public void dadoUmNomeInvalido_quandoExecutarCriarCategoria_deveSerRetornadoDomainException() {
        final String nomeEsperado = null;
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var mensagemDeErroEsperada = "'nome' não pode ser null";
        final var qtdErrosEsperado = 1;
        final var command = CriarCategoriaCommand.with(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        assertEquals(0, categoriaRepository.count());

        final var notification = useCase.execute(command).getLeft();

        assertEquals(0, categoriaRepository.count());
        assertEquals(mensagemDeErroEsperada, notification.firstError().getMessage());
        assertEquals(qtdErrosEsperado, notification.getErrors().size());
    }


    @Test
    public void dadoUmCommandValidoComCategoraInativa_quandoExecutarCriarCategoria_deveRetornaUmaCategoriaInativaComId() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = false;
        final var command = CriarCategoriaCommand.with(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        assertEquals(0, categoriaRepository.count());

        final var output = useCase.execute(command).get();
        final var idEsperado = output.getId();

        assertEquals(1, categoriaRepository.count());

        assertNotNull(output);
        assertNotNull(output.getId());

        final var categoria = categoriaRepository.findById(idEsperado).get();

        assertEquals(nomeEsperado, categoria.getNome());
        assertEquals(descricaoEsperada, categoria.getDescricao());
        assertEquals(isAtivoEsperado, categoria.isAtivo());
        assertNotNull(categoria.getDataCriacao());
        assertNotNull(categoria.getDataAtualizacao());
        assertNotNull(categoria.getDataDelecao());
    }

    @Test
    public void dadoUmCommandValido_quandoGatewayLancaException_deveRetornaUmaException() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var mensagemDeErroEsperada = "Gateway error";
        final var qtdErrosEsperado = 1;
        final var command = CriarCategoriaCommand.with(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        assertEquals(0, categoriaRepository.count());

        doThrow(new IllegalStateException(mensagemDeErroEsperada)).when(categoriaGateway).criar(any());

        final var notification = useCase.execute(command).getLeft();

        assertEquals(mensagemDeErroEsperada, notification.firstError().getMessage());
        assertEquals(qtdErrosEsperado, notification.getErrors().size());
        assertEquals(0, categoriaRepository.count());
    }
}
