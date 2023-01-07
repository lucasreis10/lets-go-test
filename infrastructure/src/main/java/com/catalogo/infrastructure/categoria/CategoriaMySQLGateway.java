package com.catalogo.infrastructure.categoria;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaGateway;
import com.catalogo.domain.categoria.CategoriaID;
import com.catalogo.domain.pagination.Pagination;
import com.catalogo.domain.pagination.SearchQuery;
import com.catalogo.infrastructure.categoria.persistence.CategoriaJpaEntity;
import com.catalogo.infrastructure.categoria.persistence.CategoriaRepository;
import com.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.Optional;

import static com.catalogo.infrastructure.utils.SpecificationUtils.like;
import static org.springframework.data.domain.Sort.Direction.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class CategoriaMySQLGateway implements CategoriaGateway {

    private final CategoriaRepository repository;

    public CategoriaMySQLGateway(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Categoria criar(final Categoria categoria) {
        return save(categoria);
    }

    @Override
    public void deletarPorId(final CategoriaID id) {
        String idValue = id.getValue();

        if (repository.existsById(idValue)) {
           repository.deleteById(idValue);
        }
    }

    @Override
    public Optional<Categoria> obterPorId(final CategoriaID id) {
        return repository.findById(id.getValue())
                .map(CategoriaJpaEntity::toAggregate);
    }

    @Override
    public Categoria atualizar(final Categoria categoria) {
        return save(categoria);
    }

    @Override
    public Pagination<Categoria> obterTodos(final SearchQuery categoriaQuery) {

        final var page = PageRequest.of(
                categoriaQuery.getPage(),
                categoriaQuery.getPerPage(),
                Sort.by(fromString(categoriaQuery.getDirection()), categoriaQuery.getSort())
        );

        final var specification = Optional.ofNullable(categoriaQuery.getTerms())
                .filter(str ->  !str.isBlank())
                .map(str -> SpecificationUtils
                        .<CategoriaJpaEntity>like("nome", str)
                        .or(like("descricao", str))
                )
                .orElse(null);

        final var pageResult =
                repository.findAll(where(specification), page);

        return new Pagination<>(
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.map(CategoriaJpaEntity::toAggregate).toList()
        );
    }

    private Categoria save(final Categoria categoria) {
        return repository.save(CategoriaJpaEntity.from(categoria)).toAggregate();
    }
}
