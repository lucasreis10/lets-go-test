package com.catalogo.application.categoria.recuperar.obter;

import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaID;

import java.time.Instant;

public class CategoriaOutput {

    private final CategoriaID id;
    private final String nome;
    private final String descricao;
    private final boolean ativo;
    private final Instant dataCriacao;
    private final Instant dataAtualizacao;
    private final Instant dataDelecao;

    private CategoriaOutput(
            CategoriaID id,
            String nome,
            String descricao,
            boolean ativo,
            Instant dataCriacao,
            Instant dataAtualizacao,
            Instant dataDelecao
    ) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.dataDelecao = dataDelecao;
    }

    public static CategoriaOutput from(Categoria categoria) {
        return new CategoriaOutput(
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
}
