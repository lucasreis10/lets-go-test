package com.catalogo.application.categoria.deletar;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.categoria.CategoriaID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeletarCategoriaUseCaseTest {

    @InjectMocks
    private DefaultDeletarCategoriaUseCase useCase;

    @Mock
    private CategoriaGateway categoriaGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoriaGateway);
    }

    @Test
    public void dadoUmIdValido_quandoChamadoDeletarCategoria_entaoTudoDeveEstarOk() {
        final var categoria = Categoria.newCategoria("Filme", "Filmes originais", true);
        final var idEsperado = categoria.getId();

        doNothing()
                .when(categoriaGateway).deletarPorId(eq(idEsperado));

        Assertions.assertDoesNotThrow(() -> useCase.execute(idEsperado.getValue()));
        verify(categoriaGateway, times(1)).deletarPorId(idEsperado);
    }

    @Test
    public void dadoUmIdInvalido_quandoChamadoDeletarCategoria_entaoTudoDeveEstarOk() {
        final var idEsperado = CategoriaID.from("123");

        doNothing()
                .when(categoriaGateway).deletarPorId(eq(idEsperado));

        Assertions.assertDoesNotThrow(() -> useCase.execute(idEsperado.getValue()));
        verify(categoriaGateway, times(1)).deletarPorId(idEsperado);
    }

    @Test
    public void dadoUmIdValido_quandoGatewayLancaException_entaoUmaExceptionEhRetornada() {
        final var categoria = Categoria.newCategoria("Filme", "Filmes originais", true);
        final var idEsperado = categoria.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(categoriaGateway).deletarPorId(eq(idEsperado));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(idEsperado.getValue()));

        verify(categoriaGateway, times(1)).deletarPorId(idEsperado);
    }



}
