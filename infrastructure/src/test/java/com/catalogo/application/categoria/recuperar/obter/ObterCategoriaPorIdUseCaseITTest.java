package com.catalogo.application.categoria.recuperar.obter;

import com.catalogo.IntegrationTest;
import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.categoria.CategoriaID;
import com.catalogo.domain.exceptions.DomainException;
import com.catalogo.domain.exceptions.NotFoundException;
import com.catalogo.infrastructure.categoria.persistence.CategoriaJpaEntity;
import com.catalogo.infrastructure.categoria.persistence.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class ObterCategoriaPorIdUseCaseITTest {

    @Autowired
    private ObterCategoriaPorIdUseCase useCase;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @SpyBean
    private CategoriaGateway categoriaGateway;

    @Test
    public void dadoUmIdValido_quandoChamadoObterCategoriaPorId_entaoRetornaCategoria() {
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;

        final var categoria = Categoria
                .newCategoria(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        final var idEsperado = categoria.getId();

        assertEquals(0, categoriaRepository.count());

        categoriaRepository.save(CategoriaJpaEntity.from(categoria));

        final var categoriaRecuperada = useCase.execute(idEsperado.getValue());

        assertEquals(idEsperado, categoriaRecuperada.getId());
        assertEquals(nomeEsperado, categoriaRecuperada.getNome());
        assertEquals(descricaoEsperada, categoriaRecuperada.getDescricao());
        assertEquals(isAtivoEsperado, categoriaRecuperada.isAtivo());
        assertEquals(categoria.getDataCriacao(), categoriaRecuperada.getDataCriacao());
        assertEquals(categoria.getDataAtualizacao(), categoriaRecuperada.getDataAtualizacao());
        assertEquals(categoria.getDataDelecao(), categoriaRecuperada.getDataDelecao());
        assertEquals(1, categoriaRepository.count());
    }

    @Test
    public void dadoUmIdInvalido_quandoChamadoObterCategoriaPorId_entaoRetornaCategoriaNotFound() {
        final var idEsperado = CategoriaID.from("123");
        final var msgErroEsperada = "Categoria com ID 123 não foi encontrado.";

        final var exceptionEsperada = assertThrows(
                NotFoundException.class,
                () -> useCase.execute(idEsperado.getValue())
        );

        assertEquals(msgErroEsperada, exceptionEsperada.getMessage());
    }

    @Test
    public void dadoUmIdValido_quandoGatewayLancaException_entaoUmaExceptionEhRetornada() {
        final var idEsperado = CategoriaID.from("123");
        final var msgErroEsperada = "Gateway erro";

        doThrow(new IllegalStateException(msgErroEsperada))
                .when(categoriaGateway).obterPorId(Mockito.eq(idEsperado));

        final var exceptionEsperada = assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(idEsperado.getValue())
        );

        assertEquals(msgErroEsperada, exceptionEsperada.getMessage());
    }

}
