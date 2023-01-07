package com.catalogo.application.categoria.deletar;

import com.catalogo.IntegrationTest;
import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.categoria.CategoriaID;
import com.catalogo.infrastructure.categoria.persistence.CategoriaJpaEntity;
import com.catalogo.infrastructure.categoria.persistence.CategoriaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeletarCategoriaUseCaseITTest {

    @Autowired
    private DeletarCategoriaUseCase useCase;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @SpyBean
    private CategoriaGateway categoriaGateway;

    @Test
    public void dadoUmIdValido_quandoChamadoDeletarCategoria_entaoTudoDeveEstarOk() {
        final var categoria = Categoria.newCategoria("Filme", "Filmes originais", true);
        final var idEsperado = categoria.getId();

        categoriaRepository.save(CategoriaJpaEntity.from(categoria));

        assertEquals(1, categoriaRepository.count());

        useCase.execute(idEsperado.getValue());

        assertEquals(0, categoriaRepository.count());
    }

    @Test
    public void dadoUmIdInvalido_quandoChamadoDeletarCategoria_entaoTudoDeveEstarOk() {
        final var idEsperado = CategoriaID.from("123");

        assertEquals(0, categoriaRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(idEsperado.getValue()));

        assertEquals(0, categoriaRepository.count());
    }


    @Test
    public void dadoUmIdValido_quandoGatewayLancaException_entaoUmaExceptionEhRetornada() {
        final var categoria = Categoria.newCategoria("Filme", "Filmes originais", true);
        final var idEsperado = categoria.getId();
        final var msgErroEsperada = "Gateway error";

        categoriaRepository.save(CategoriaJpaEntity.from(categoria));

        assertEquals(1, categoriaRepository.count());

        doThrow(new IllegalStateException("Gateway error"))
                .when(categoriaGateway).deletarPorId(eq(idEsperado));

        final var exceptionEsperada = assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(idEsperado.getValue())
        );

        assertEquals(msgErroEsperada, exceptionEsperada.getMessage());
        assertEquals(1, categoriaRepository.count());
    }


}
