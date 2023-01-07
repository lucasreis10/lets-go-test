package com.catalogo.infrastructure.categoria.persistence;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "categoria")
public class CategoriaJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 4000)
    private String descricao;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "data_criacao", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant dataCriacao;

    @Column(name = "data_atualizacao", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant dataAtualizacao;

    @Column(name = "data_delecao", columnDefinition = "DATETIME(6)")
    private Instant dataDelecao;


    public CategoriaJpaEntity() {
    }

    private CategoriaJpaEntity(String id, String nome, String descricao, boolean ativo, Instant dataCriacao, Instant dataAtualizacao, Instant dataDelecao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.dataDelecao = dataDelecao;
    }

    public static CategoriaJpaEntity from(final Categoria categoria) {
        return new CategoriaJpaEntity(
                categoria.getId().getValue(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.isAtivo(),
                categoria.getDataCriacao(),
                categoria.getDataAtualizacao(),
                categoria.getDataDelecao()
        );
    }

    public Categoria toAggregate() {
        return Categoria.with(
                CategoriaID.from(getId()),
                getNome(),
                getDescricao(),
                isAtivo(),
                getDataCriacao(),
                getDataAtualizacao(),
                getDataDelecao()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Instant dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Instant getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Instant dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Instant getDataDelecao() {
        return dataDelecao;
    }

    public void setDataDelecao(Instant dataDelecao) {
        this.dataDelecao = dataDelecao;
    }
}
