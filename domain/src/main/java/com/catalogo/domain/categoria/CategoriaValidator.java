package com.catalogo.domain.categoria;

import com.catalogo.domain.validation.Error;
import com.catalogo.domain.validation.ValidationHandler;
import com.catalogo.domain.validation.Validator;

public class CategoriaValidator extends Validator {

    private static final int TAMANHO_MIN_NOME = 3;
    private static final int TAMANHO_MAX_NOME = 255;
    private final Categoria categoria;

    public CategoriaValidator(final Categoria categoria, final ValidationHandler handler) {
        super(handler);
        this.categoria = categoria;
    }

    @Override
    public void validate() {
        checarRegrasDoNome();
    }

    private void checarRegrasDoNome() {
        final var nome = this.categoria.getNome();

        if(nome == null) {
            this.validationHandler().append(new Error("'nome' não pode ser null"));
            return;
        }

        if(nome.isBlank()) {
            this.validationHandler().append(new Error("'nome' não pode ser vazio"));
            return;
        }

        final var tamanhoDoNome = nome.trim().length();
        if(tamanhoDoNome < TAMANHO_MIN_NOME || tamanhoDoNome > TAMANHO_MAX_NOME) {
            this.validationHandler().append(new Error("'nome' deve ter de 3 a 255 caracteres"));
        }
    }
}
