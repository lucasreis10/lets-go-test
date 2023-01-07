package com.catalogo.domain.categoria;

import com.catalogo.domain.AggregateRoot;
import com.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Categoria extends AggregateRoot<CategoriaID> implements Cloneable {
    private String nome;
    private String descricao;
    private boolean ativo;
    private Instant dataCriacao;
    private Instant dataAtualizacao;
    private Instant dataDelecao;

    private Categoria(
            final CategoriaID id,
            final String nome,
            final String descricao,
            final boolean ativo,
            final Instant dataCriacao,
            final Instant dataAtualizacao,
            final Instant dataDelecao
    ) {
        super(id);
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
        this.dataCriacao = Objects.requireNonNull(dataCriacao, "'dataCriacao' não pode ser null");
        this.dataAtualizacao =  Objects.requireNonNull(dataAtualizacao, "'dataAtualizacao' não pode ser null");
        this.dataDelecao = dataDelecao;
    }

    public static Categoria newCategoria(String nome, String descricao, boolean ativo) {
        final var id = CategoriaID.unique();
        final var now = Instant.now();
        final var deletedAt = ativo ? null : now;
        return new Categoria(id, nome, descricao, ativo, now, now, deletedAt);
    }

    public static Categoria with(final Categoria categoria) {
        return with(
            categoria.getId(),
            categoria.getNome(),
            categoria.getDescricao(),
            categoria.isAtivo(),
            categoria.getDataCriacao(),
            categoria.getDataAtualizacao(),
            categoria.getDataDelecao()
        );
    }

    public static Categoria with(
            final CategoriaID id,
            final String nome,
            final String descricao,
            final boolean ativo,
            final Instant dataCriacao,
            final Instant dataAtualizacao,
            final Instant dataDelecao
    ) {
        return new Categoria(
                id,
                nome,
                descricao,
                ativo,
                dataCriacao,
                dataAtualizacao,
                dataDelecao
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoriaValidator(this, handler).validate();
    }

    public Categoria desativar() {
        if(getDataDelecao() == null) {
            this.dataDelecao = Instant.now();
        }

        this.ativo = false;
        this.dataAtualizacao = Instant.now();
        return this;
    }

    public Categoria ativar() {
        this.dataDelecao = null;
        this.dataAtualizacao = Instant.now();
        this.ativo = true;
        return this;
    }

    public Categoria atualizar(String nome, String descricao, boolean ativo) {
        if(ativo) {
            ativar();
        } else {
            desativar();
        }
        this.nome = nome;
        this.descricao = descricao;
        return this;
    }

    public CategoriaID getId() {
        return (CategoriaID) id;
    }
    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public boolean isAtivo() {
        return ativo;
    }
    public Instant getDataCriacao() {
        return dataCriacao;
    }
    public Instant getDataAtualizacao() {
        return dataAtualizacao;
    }
    public Instant getDataDelecao() {
        return dataDelecao;
    }

    @Override
    public Categoria clone() {
        try {
            return (Categoria) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
