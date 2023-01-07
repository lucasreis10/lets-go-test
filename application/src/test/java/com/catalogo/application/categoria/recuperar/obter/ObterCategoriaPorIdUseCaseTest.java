package com.catalogo.application.categoria.recuperar.obter;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.categoria.CategoriaID;
import com.catalogo.domain.exceptions.DomainException;
import com.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ObterCategoriaPorIdUseCaseTest {

    @InjectMocks
    private DefaultObterCategoriaPorIdUseCase useCase;

    @Mock
    private CategoriaGateway categoriaGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoriaGateway);
    }

    @Test
    public void dadoUmIdValido_quandoChamadoObterCategoriaPorId_entaoRetornaCategoria() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;

        final var categoria = Categoria
                .newCategoria(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        final var idEsperado = categoria.getId();

        Mockito.when(categoriaGateway.obterPorId(Mockito.eq(idEsperado)))
                .thenReturn(Optional.of(categoria.clone()));

        final var categoriaRecuperada = useCase.execute(idEsperado.getValue());

        assertEquals(idEsperado, categoriaRecuperada.getId());
        assertEquals(nomeEsperado, categoriaRecuperada.getNome());
        assertEquals(descricaoEsperada, categoriaRecuperada.getDescricao());
        assertEquals(isAtivoEsperado, categoriaRecuperada.isAtivo());
        assertEquals(categoria.getDataCriacao(), categoriaRecuperada.getDataCriacao());
        assertEquals(categoria.getDataAtualizacao(), categoriaRecuperada.getDataAtualizacao());
        assertEquals(categoria.getDataDelecao(), categoriaRecuperada.getDataDelecao());
    }

    @Test
    public void dadoUmIdInvalido_quandoChamadoObterCategoriaPorId_entaoRetornaCategoriaNotFound() {
        final var idEsperado = "dummy";
        final var msgErroEsperada = "Categoria com ID dummy não foi encontrado.";

        Mockito.when(categoriaGateway.obterPorId(Mockito.eq(CategoriaID.from(idEsperado))))
                .thenReturn(Optional.empty());

        final var exceptionEsperada = assertThrows(
                NotFoundException.class,
                () -> useCase.execute(idEsperado)
        );

        assertEquals(msgErroEsperada, exceptionEsperada.getMessage());
    }

    @Test
    public void dadoUmIdValido_quandoGatewayLancaException_entaoUmaExceptionEhRetornada() {
        final var idEsperado = "Gateway error";
        final var msgErroEsperada = "";

        Mockito.when(categoriaGateway.obterPorId(Mockito.eq(CategoriaID.from(idEsperado))))
                .thenThrow(new IllegalStateException(msgErroEsperada));

        final var exceptionEsperada = assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(idEsperado)
        );

        assertEquals(msgErroEsperada, exceptionEsperada.getMessage());
    }
}
