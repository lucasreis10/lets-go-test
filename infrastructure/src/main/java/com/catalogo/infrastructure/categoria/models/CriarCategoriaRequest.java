package com.catalogo.infrastructure.categoria.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CriarCategoriaRequest {

    @JsonProperty("nome")
    private String nome;
    @JsonProperty("descricao")
    private String descricao;
    @JsonProperty("is_ativo")
    private Boolean ativo;


    public CriarCategoriaRequest() {
    }

    public CriarCategoriaRequest(String nome, String descricao, Boolean ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
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
}
