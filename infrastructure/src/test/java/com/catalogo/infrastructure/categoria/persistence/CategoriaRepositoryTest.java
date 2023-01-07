package com.catalogo.infrastructure.categoria.persistence;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MySQLGatewayTest
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;


    @Test
    public void dadoNomeNullInvalido_quandoExecutarSave_entaoRetornaErro() {
        final var nomeDePropriedadeEsperado = "nome";
        final var mensagemErroEsperada = "not-null property references a null or transient value : com.catalogo.infrastructure.categoria.persistence.CategoriaJpaEntity.nome";

        final var categoria = Categoria.newCategoria("Filmes", null, true);

        final var categoriaEntity = CategoriaJpaEntity.from(categoria);
        categoriaEntity.setNome(null);

        final var exceptionEsperada =
                assertThrows(DataIntegrityViolationException.class, () -> categoriaRepository.save(categoriaEntity));

        final var cause =
                assertInstanceOf(PropertyValueException.class, exceptionEsperada.getCause());

        Assertions.assertEquals(nomeDePropriedadeEsperado, cause.getPropertyName());
        Assertions.assertEquals(mensagemErroEsperada, cause.getMessage());

    }

    @Test
    public void dadoDataCriacaoNullInvalido_quandoExecutarSave_entaoRetornaErro() {
        final var nomeDePropriedadeEsperado = "dataCriacao";
        final var mensagemErroEsperada = "not-null property references a null or transient value : com.catalogo.infrastructure.categoria.persistence.CategoriaJpaEntity.dataCriacao";

        final var categoria = Categoria.newCategoria("Filmes", null, true);

        final var categoriaEntity = CategoriaJpaEntity.from(categoria);
        categoriaEntity.setDataCriacao(null);

        final var exceptionEsperada =
                assertThrows(DataIntegrityViolationException.class, () -> categoriaRepository.save(categoriaEntity));

        final var cause =
                assertInstanceOf(PropertyValueException.class, exceptionEsperada.getCause());

        Assertions.assertEquals(nomeDePropriedadeEsperado, cause.getPropertyName());
        Assertions.assertEquals(mensagemErroEsperada, cause.getMessage());

    }

    @Test
    public void dadoDataAtualziacaoNullInvalido_quandoExecutarSave_entaoRetornaErro() {
        final var nomeDePropriedadeEsperado = "dataAtualizacao";
        final var mensagemErroEsperada = "not-null property references a null or transient value : com.catalogo.infrastructure.categoria.persistence.CategoriaJpaEntity.dataAtualizacao";

        final var categoria = Categoria.newCategoria("Filmes", null, true);

        final var categoriaEntity = CategoriaJpaEntity.from(categoria);
        categoriaEntity.setDataAtualizacao(null);

        final var exceptionEsperada =
                assertThrows(DataIntegrityViolationException.class, () -> categoriaRepository.save(categoriaEntity));

        final var cause =
                assertInstanceOf(PropertyValueException.class, exceptionEsperada.getCause());

        Assertions.assertEquals(nomeDePropriedadeEsperado, cause.getPropertyName());
        Assertions.assertEquals(mensagemErroEsperada, cause.getMessage());

    }

}
