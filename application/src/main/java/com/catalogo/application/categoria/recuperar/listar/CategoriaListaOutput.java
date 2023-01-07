package com.catalogo.application.categoria.recuperar.listar;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaID;

import java.time.Instant;
import java.util.Objects;

public class CategoriaListaOutput {
    private CategoriaID id;
    private String nome;
    private String descricao;
    private boolean isAtivo;
    private Instant dataCriacao;
    private Instant dataAtualizacao;
    private Instant dataDelecao;

    public CategoriaListaOutput(CategoriaID id, String nome, String descricao, boolean isAtivo, Instant dataCriacao, Instant dataAtualizacao, Instant dataDelecao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.isAtivo = isAtivo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.dataDelecao = dataDelecao;
    }

    public static CategoriaListaOutput from(final Categoria categoria) {
        return new CategoriaListaOutput(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.isAtivo(),
                categoria.getDataCriacao(),
                categoria.getDataAtualizacao(),
                categoria.getDataDelecao()
        );
    }

    public CategoriaID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isAtivo() {
        return isAtivo;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaListaOutput that = (CategoriaListaOutput) o;
        return isAtivo() == that.isAtivo() && Objects.equals(getId(), that.getId()) && Objects.equals(getNome(), that.getNome()) && Objects.equals(getDescricao(), that.getDescricao()) && Objects.equals(getDataCriacao(), that.getDataCriacao()) && Objects.equals(getDataAtualizacao(), that.getDataAtualizacao()) && Objects.equals(getDataDelecao(), that.getDataDelecao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNome(), getDescricao(), isAtivo(), getDataCriacao(), getDataAtualizacao(), getDataDelecao());
    }
}
