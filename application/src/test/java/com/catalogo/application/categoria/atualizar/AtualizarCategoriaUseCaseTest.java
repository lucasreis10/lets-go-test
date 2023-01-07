package com.catalogo.application.categoria.atualizar;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.categoria.CategoriaID;
import com.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AtualizarCategoriaUseCaseTest {

    @InjectMocks
    private DefaultAtualizarCategoriaUseCase useCase;

    @Mock
    private CategoriaGateway categoriaGateway;


    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoriaGateway);
    }

    @Test
    public void dadoUmCommandValido_quandoExecutadoAtualizarCategoria_deveRetornaCategoriaId() {
        final var categoria = Categoria
                .newCategoria("Serie", null, true);

        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var idEsperado = categoria.getId();
        final var isAtivoEsperado = true;


        final var command = AtualizarCategoriaCommand
                .with(idEsperado.getValue(), nomeEsperado, descricaoEsperada, isAtivoEsperado);

        when(categoriaGateway.obterPorId(eq(idEsperado)))
                .thenReturn(Optional.of(Categoria.with(categoria)));

        when(categoriaGateway.atualizar(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.getId());
        verify(categoriaGateway, times(1)).obterPorId(eq(idEsperado));
        verify(categoriaGateway, times(1))
                .atualizar(argThat(umaCatetoriaAtualizada ->
                           Objects.equals(umaCatetoriaAtualizada.getNome(), nomeEsperado)
                                && Objects.equals(umaCatetoriaAtualizada.getDescricao(), descricaoEsperada)
                                && Objects.equals(umaCatetoriaAtualizada.isAtivo(), isAtivoEsperado)
                                && Objects.equals(umaCatetoriaAtualizada.getId(), idEsperado)
                                && Objects.equals(umaCatetoriaAtualizada.getDataCriacao(), categoria.getDataCriacao())
                                && categoria.getDataAtualizacao().isBefore(umaCatetoriaAtualizada.getDataAtualizacao())
                                && Objects.isNull(umaCatetoriaAtualizada.getDataDelecao())
                ));
    }

    @Test
    public void dadoUmNomeInvalido_quandoExecutarAtualizarCategoria_deveSerRetornadoDomainException() {
        final String nomeEsperado = null;
        final var idEsperado = CategoriaID.unique();
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var mensagemDeErroEsperada = "'nome' não pode ser null";
        final var qtdErrosEsperado = 1;
        final var command = AtualizarCategoriaCommand.with(idEsperado.getValue(), nomeEsperado, descricaoEsperada, isAtivoEsperado);
        final var categoria = Categoria.newCategoria("Serie", null, true);

        when(categoriaGateway.obterPorId(eq(idEsperado)))
                .thenReturn(Optional.of(categoria.clone()));

        final var notification = useCase.execute(command).getLeft();

        assertEquals(mensagemDeErroEsperada, notification.firstError().getMessage());
        assertEquals(qtdErrosEsperado, notification.getErrors().size());
        verify(categoriaGateway, times(0)).atualizar(any());
    }

    @Test
    public void dadoCommandValidoComCategoraInativa_quandoExecutarAtualizarCategoria_deveRetornaUmaCategoriaInativaComId() {
        final var categoria = Categoria
                .newCategoria("Serie", null, true);

        assertTrue(categoria.isAtivo());
        assertNull(categoria.getDataDelecao());

        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var idEsperado = categoria.getId();
        final var isAtivoEsperado = false;


        final var command = AtualizarCategoriaCommand
                .with(idEsperado.getValue(), nomeEsperado, descricaoEsperada, isAtivoEsperado);

        when(categoriaGateway.obterPorId(eq(idEsperado)))
                .thenReturn(Optional.of(Categoria.with(categoria)));

        when(categoriaGateway.atualizar(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var output = useCase.execute(command).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.getId());
        verify(categoriaGateway, times(1)).obterPorId(eq(idEsperado));
        verify(categoriaGateway, times( 1))
                .atualizar(argThat(umaCatetoriaAtualizada ->
                        Objects.equals(umaCatetoriaAtualizada.getNome(), nomeEsperado)
                                && Objects.equals(umaCatetoriaAtualizada.getDescricao(), descricaoEsperada)
                                && Objects.equals(umaCatetoriaAtualizada.isAtivo(), isAtivoEsperado)
                                && Objects.equals(umaCatetoriaAtualizada.getId(), idEsperado)
                                && Objects.equals(umaCatetoriaAtualizada.getDataCriacao(), categoria.getDataCriacao())
                                && categoria.getDataAtualizacao().isBefore(umaCatetoriaAtualizada.getDataAtualizacao())
                                && Objects.nonNull(umaCatetoriaAtualizada.getDataDelecao())
                ));
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


        when(categoriaGateway.obterPorId(eq(idEsperado)))
                .thenReturn(Optional.of(Categoria.with(categoria)));

        when(categoriaGateway.atualizar(any()))
                .thenThrow(new IllegalStateException(mensagemDeErroEsperada));

        final var notification = useCase.execute(command).getLeft();

        assertEquals(mensagemDeErroEsperada, notification.firstError().getMessage());
        assertEquals(qtdErrosEsperado, notification.getErrors().size());
        verify(categoriaGateway, times( 1))
                .atualizar(argThat(umaCatetoriaAtualizada ->
                        Objects.equals(umaCatetoriaAtualizada.getNome(), nomeEsperado)
                                && Objects.equals(umaCatetoriaAtualizada.getDescricao(), descricaoEsperada)
                                && Objects.equals(umaCatetoriaAtualizada.isAtivo(), isAtivoEsperado)
                                && Objects.equals(umaCatetoriaAtualizada.getId(), idEsperado)
                                && Objects.equals(umaCatetoriaAtualizada.getDataCriacao(), categoria.getDataCriacao())
                                && categoria.getDataAtualizacao().isBefore(umaCatetoriaAtualizada.getDataAtualizacao())
                                && Objects.isNull(umaCatetoriaAtualizada.getDataDelecao())
                ));
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

        when(categoriaGateway.obterPorId(eq(CategoriaID.from(idEsperado))))
                .thenReturn(Optional.empty());


        final var domainException = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertEquals(msgErroEsperada, domainException.getMessage());

        verify(categoriaGateway, times(1)).obterPorId(eq(CategoriaID.from(idEsperado)));
        verify(categoriaGateway, times( 0)).atualizar(any());
    }


}
