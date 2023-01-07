package com.catalogo.application.categoria.criar;

public class CriarCategoriaCommand {
    
    private final String nome;
    private final String descricao;
    private final boolean isAtivo;


    private CriarCategoriaCommand(String nome, String descricao, boolean isAtivo) {
        this.nome = nome;
        this.descricao = descricao;
        this.isAtivo = isAtivo;
    }

    public static CriarCategoriaCommand with(final String nome, final String descricao, final boolean isAtivo) {
        return new CriarCategoriaCommand(nome, descricao, isAtivo);
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
}
