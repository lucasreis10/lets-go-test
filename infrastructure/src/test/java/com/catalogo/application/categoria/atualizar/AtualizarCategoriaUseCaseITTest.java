package com.catalogo.application.categoria.atualizar;

import com.catalogo.IntegrationTest;
import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.exceptions.NotFoundException;
import com.catalogo.infrastructure.categoria.persistence.CategoriaJpaEntity;
import com.catalogo.infrastructure.categoria.persistence.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class AtualizarCategoriaUseCaseITTest {

    @Autowired
    private AtualizarCategoriaUseCase useCase;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @SpyBean
    private CategoriaGateway categoriaGateway;

    @Test
    public void dadoUmCommandValido_quandoExecutadoAtualizarCategoria_deveRetornaCategoriaId() {
        final var categoria = Categoria
                .newCategoria("Serie", null, true);

        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var idEsperado = categoria.getId();
        final var isAtivoEsperado = true;

        categoriaRepository.save(CategoriaJpaEntity.from(categoria));

        assertEquals(1, categoriaRepository.count());

        final var command = AtualizarCategoriaCommand
                .with(idEsperado.getValue(), nomeEsperado, descricaoEsperada, isAtivoEsperado);

        final var output = useCase.execute(command).get();

        final var categoriaRecuperada =
                categoriaRepository.findById(categoria.getId().getValue()).get();

        assertNotNull(output);
        assertNotNull(output.getId());
        assertEquals(idEsperado.getValue(), categoriaRecuperada.getId());
        assertEquals(nomeEsperado, categoriaRecuperada.getNome());
        assertEquals(descricaoEsperada, categoriaRecuperada.getDescricao());
        assertEquals(isAtivoEsperado, categoriaRecuperada.isAtivo());
        assertNotNull(categoriaRecuperada.getDataCriacao());
        assertTrue(categoriaRecuperada.getDataAtualizacao().isAfter(categoria.getDataAtualizacao()));
        assertNull(categoriaRecuperada.getDataDelecao());
        assertEquals(1, categoriaRepository.count());
    }

    @Test
    public void dadoUmNomeInvalido_quandoExecutarAtualizarCategoria_deveSerRetornadoDomainException() {
        final String nomeEsperado = null;
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var mensagemDeErroEsperada = "'nome' não pode ser null";
        final var qtdErrosEsperado = 1;
        final var categoria = Categoria.newCategoria("Serie", null, true);
        final var idEsperado = categoria.getId();
        final var command = AtualizarCategoriaCommand.with(idEsperado.getValue(), nomeEsperado, descricaoEsperada, isAtivoEsperado);

        categoriaRepository.save(CategoriaJpaEntity.from(categoria));

        assertEquals(1, categoriaRepository.count());

        final var notification = useCase.execute(command).getLeft();

        assertEquals(mensagemDeErroEsperada, notification.firstError().getMessage());
        assertEquals(qtdErrosEsperado, notification.getErrors().size());
        verify(categoriaGateway, times(0)).atualizar(any());
        assertEquals(1, categoriaRepository.count());
    }

    @Test
    public void dadoUmCommandValido_quandoGatewayLancaException_deveRetornaUmaException() {
        final var categoria = Categoria
                .newCategoria("Serie", null, true);

        final var nomeEsperado = "Filmes";
        final var idEsperado = categoria.getId();
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var mensagemDeErroEsperada = "Gateway error";
        final var qtdErrosEsperado = 1;

        final var command = AtualizarCategoriaCommand
                .with(idEsperado.getValue(), nomeEsperado, descricaoEsperada, isAtivoEsperado);

        categoriaRepository.save(CategoriaJpaEntity.from(categoria));

        doThrow(new IllegalStateException(mensagemDeErroEsperada))
                .when(categoriaGateway).atualizar(any());

        final var notification = useCase.execute(command).getLeft();

        assertEquals(mensagemDeErroEsperada, notification.firstError().getMessage());
        assertEquals(qtdErrosEsperado, notification.getErrors().size());
    }

    @Test
    public void dadoUmIdInvalido_quandoExecutarAtualizarCategoria_deveRetornaCategoriaNotFound() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var idEsperado = "123";
        final var isAtivoEsperado = false;
        final var msgErroEsperada = "Categoria com ID 123 não foi encontrado.";

        final var command = AtualizarCategoriaCommand
                .with(idEsperado, nomeEsperado, descricaoEsperada, isAtivoEsperado);

        final var domainException = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(msgErroEsperada, domainException.getMessage());
        assertEquals(0, categoriaRepository.count());

    }
}
