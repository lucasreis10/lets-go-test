package com.catalogo.infrastructure.categoria.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaJpaEntity, String> {

    Page<CategoriaJpaEntity> findAll(Specification<CategoriaJpaEntity> whereClause, Pageable page);

}
