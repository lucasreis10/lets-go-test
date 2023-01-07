package com.catalogo.application.categoria.criar;


import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CriarCategoriaUseCaseTest {

    @InjectMocks
    private DefaultCriarCategoriaUseCase useCase;
    @Mock
    private CategoriaGateway categoriaGateway;


    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoriaGateway);
    }

    @Test
    public void dadoUmCommandValido_quandoExecutarCriarCategoria_deveRetornaUmaCategoriaComId() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var command = CriarCategoriaCommand.with(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        when(categoriaGateway.criar(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.getId());

        verify(categoriaGateway, times(1))
                .criar(argThat(categoria -> Objects.equals(nomeEsperado, categoria.getNome())
                        && Objects.equals(descricaoEsperada, categoria.getDescricao())
                        && Objects.equals(isAtivoEsperado, categoria.isAtivo())
                        && Objects.nonNull(categoria.getId())
                        && Objects.nonNull(categoria.getDataCriacao())
                        && Objects.nonNull(categoria.getDataAtualizacao())
                        && Objects.isNull(categoria.getDataDelecao())
                ));
    }

    @Test
    public void dadoUmNomeInvalido_quandoExecutarCriarCategoria_deveSerRetornadoDomainException() {
        final String nomeEsperado = null;
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var mensagemDeErroEsperada = "'nome' não pode ser null";
        final var qtdErrosEsperado = 1;
        final var command = CriarCategoriaCommand.with(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        final var notification = useCase.execute(command).getLeft();

        assertEquals(mensagemDeErroEsperada, notification.firstError().getMessage());
        assertEquals(qtdErrosEsperado, notification.getErrors().size());
        verify(categoriaGateway, times(0)).criar(any());
    }

    @Test
    public void dadoUmCommandValidoComCategoraInativa_quandoExecutarCriarCategoria_deveRetornaUmaCategoriaInativaComId() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = false;
        final var categoriaEsperada = Categoria.newCategoria(nomeEsperado, descricaoEsperada, isAtivoEsperado);
        final var command = CriarCategoriaCommand.with(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        when(categoriaGateway.criar(any())).thenReturn(categoriaEsperada);

        final var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.getId());

        verify(categoriaGateway, times(1))
                .criar(argThat(categoria -> Objects.equals(nomeEsperado, categoria.getNome())
                        && Objects.equals(descricaoEsperada, categoria.getDescricao())
                        && Objects.equals(isAtivoEsperado, categoria.isAtivo())
                        && Objects.nonNull(categoria.getId())
                        && Objects.nonNull(categoria.getDataCriacao())
                        && Objects.nonNull(categoria.getDataAtualizacao())
                        && Objects.nonNull(categoria.getDataDelecao())
                ));
    }

    @Test
    public void dadoUmCommandValido_quandoGatewayLancaException_deveRetornaUmaException() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var mensagemDeErroEsperada = "Gateway error";
        final var qtdErrosEsperado = 1;
        final var command = CriarCategoriaCommand.with(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        when(categoriaGateway.criar(any())).thenThrow(new IllegalStateException(mensagemDeErroEsperada));

        final var notification = useCase.execute(command).getLeft();

        assertEquals(mensagemDeErroEsperada, notification.firstError().getMessage());
        assertEquals(qtdErrosEsperado, notification.getErrors().size());
        verify(categoriaGateway, times(1)).criar(any());
    }
}
