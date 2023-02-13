package com.catalogo.infrastructure.categoria.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ListarCategoriaResponse {

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
    @JsonProperty("data_delecao")
    private Instant dataDelecao;

    public ListarCategoriaResponse(
            String id,
            String nome,
            String descricao,
            Boolean ativo,
            Instant dataCriacao,
            Instant dataDelecao
    ) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.dataDelecao = dataDelecao;
    }
}
