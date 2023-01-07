package com.catalogo.application.categoria.atualizar;

public class AtualizarCategoriaCommand {

    private final String id;
    private final String nome;
    private final String descricao;
    private final boolean isAtivo;

    private AtualizarCategoriaCommand(String id, String nome, String descricao, boolean isAtivo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.isAtivo = isAtivo;
    }

    public static AtualizarCategoriaCommand with(final String id, final String nome, final String descricao, final boolean isAtivo) {
        return new AtualizarCategoriaCommand(id, nome, descricao, isAtivo);
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

    public boolean isAtivo() {
        return isAtivo;
    }
}
