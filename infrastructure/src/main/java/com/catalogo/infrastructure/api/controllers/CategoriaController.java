package com.catalogo.infrastructure.api.controllers;

import com.catalogo.application.categoria.atualizar.AtualizarCategoriaCommand;
import com.catalogo.application.categoria.atualizar.AtualizarCategoriaOutput;
import com.catalogo.application.categoria.atualizar.AtualizarCategoriaUseCase;
import com.catalogo.application.categoria.criar.CriarCategoriaCommand;
import com.catalogo.application.categoria.criar.CriarCategoriaOutput;
import com.catalogo.application.categoria.criar.CriarCategoriaUseCase;
import com.catalogo.application.categoria.deletar.DeletarCategoriaUseCase;
import com.catalogo.application.categoria.recuperar.obter.ObterCategoriaPorIdUseCase;
import com.catalogo.domain.pagination.Pagination;
import com.catalogo.domain.validation.handler.Notification;
import com.catalogo.infrastructure.api.CategoriaAPI;
import com.catalogo.infrastructure.categoria.models.AtualizarCategoriaAPIInput;
import com.catalogo.infrastructure.categoria.models.CategoriaAPIOutput;
import com.catalogo.infrastructure.categoria.models.CriarCategoriaAPIInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoriaController implements CategoriaAPI {

    private CriarCategoriaUseCase criarCategoriaUseCase;
    private ObterCategoriaPorIdUseCase obterCategoriaPorIdUseCase;
    private AtualizarCategoriaUseCase atualizarCategoriaUseCase;
    private DeletarCategoriaUseCase deletarCategoriaUseCase;

    public CategoriaController(
            final CriarCategoriaUseCase criarCategoriaUseCase,
            final ObterCategoriaPorIdUseCase obterCategoriaPorIdUseCase,
            final AtualizarCategoriaUseCase atualizarCategoriaUseCase,
            final DeletarCategoriaUseCase deletarCategoriaUseCase) {
        this.criarCategoriaUseCase = Objects.requireNonNull(criarCategoriaUseCase);
        this.obterCategoriaPorIdUseCase = obterCategoriaPorIdUseCase;
        this.atualizarCategoriaUseCase = atualizarCategoriaUseCase;
        this.deletarCategoriaUseCase = deletarCategoriaUseCase;
    }

    @Override
    public ResponseEntity<?> criarCategoria(final CriarCategoriaAPIInput input) {
        final var command =
                CriarCategoriaCommand.with(
                        input.getNome(),
                        input.getDescricao(),
                        input.isAtivo() != null ? input.isAtivo() : true
                );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CriarCategoriaOutput, ResponseEntity<?>> onSucess = output ->
                ResponseEntity.created(URI.create("/categorias/" + output.getId())).body(output);

        return  criarCategoriaUseCase.execute(command)
                .fold(onError, onSucess);
    }

    @Override
    public Pagination<?> listarCategorias(
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        return null;
    }

    @Override
    public CategoriaAPIOutput obterPorId(final String id) {
        return CategoriaAPIOutput.from(obterCategoriaPorIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> atualizarPorId(final String id, final AtualizarCategoriaAPIInput input) {
        final var command =
                AtualizarCategoriaCommand.with(
                        id,
                        input.getNome(),
                        input.getDescricao(),
                        input.isAtivo() != null ? input.isAtivo() : true
                );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<AtualizarCategoriaOutput, ResponseEntity<?>> onSucess =
                ResponseEntity::ok;

        return  atualizarCategoriaUseCase.execute(command)
                .fold(onError, onSucess);
    }

    @Override
    public void deletarPorId(final String id) {
        deletarCategoriaUseCase.execute(id);
    }
}
