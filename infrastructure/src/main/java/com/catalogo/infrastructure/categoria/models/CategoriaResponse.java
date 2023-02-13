package com.catalogo.infrastructure.categoria.models;

import com.catalogo.application.categoria.recuperar.obter.CategoriaOutput;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class CategoriaResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("nome")
    private String nome;
    @JsonProperty("descricao")
    private String descricao;
    @JsonProperty("is_ativo")
    private Boolean ativo;
    @JsonProperty("data_criacao")
    private Instant dataCriacao;
    @JsonProperty("data_atualizacao")
    private Instant dataAtualizacao;
    @JsonProperty("data_delecao")
    private Instant dataDelecao;

    public CategoriaResponse(
            String id,
            String nome,
            String descricao,
            Boolean ativo,
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

    public CategoriaResponse() {
    }

    public static CategoriaResponse from(CategoriaOutput categoriaOutput) {
        return new CategoriaResponse(
                categoriaOutput.getId().getValue(),
                categoriaOutput.getNome(),
                categoriaOutput.getDescricao(),
                categoriaOutput.isAtivo(),
                categoriaOutput.getDataCriacao(),
                categoriaOutput.getDataAtualizacao(),
                categoriaOutput.getDataDelecao()
        );
    }


    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Boolean isAtivo() {
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
