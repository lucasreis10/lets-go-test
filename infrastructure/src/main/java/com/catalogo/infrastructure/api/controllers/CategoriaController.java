package com.catalogo.infrastructure.api.controllers;

import com.catalogo.application.categoria.atualizar.AtualizarCategoriaCommand;
import com.catalogo.application.categoria.atualizar.AtualizarCategoriaOutput;
import com.catalogo.application.categoria.atualizar.AtualizarCategoriaUseCase;
import com.catalogo.application.categoria.criar.CriarCategoriaCommand;
import com.catalogo.application.categoria.criar.CriarCategoriaOutput;
import com.catalogo.application.categoria.criar.CriarCategoriaUseCase;
import com.catalogo.application.categoria.deletar.DeletarCategoriaUseCase;
import com.catalogo.application.categoria.recuperar.listar.ListarCategoriaUseCase;
import com.catalogo.application.categoria.recuperar.obter.ObterCategoriaPorIdUseCase;
import com.catalogo.domain.pagination.Pagination;
import com.catalogo.domain.pagination.SearchQuery;
import com.catalogo.domain.validation.handler.Notification;
import com.catalogo.infrastructure.api.CategoriaAPI;
import com.catalogo.infrastructure.categoria.models.AtualizarCategoriaResquest;
import com.catalogo.infrastructure.categoria.models.CategoriaResponse;
import com.catalogo.infrastructure.categoria.models.CriarCategoriaRequest;
import com.catalogo.infrastructure.categoria.models.ListarCategoriaResponse;
import com.catalogo.infrastructure.categoria.presenters.CategoriaPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@RestController
public class CategoriaController implements CategoriaAPI {

    private final CriarCategoriaUseCase criarCategoriaUseCase;
    private final ObterCategoriaPorIdUseCase obterCategoriaPorIdUseCase;
    private final AtualizarCategoriaUseCase atualizarCategoriaUseCase;
    private final DeletarCategoriaUseCase deletarCategoriaUseCase;
    private final ListarCategoriaUseCase listarCategoriaUseCase;

    public CategoriaController(
            final CriarCategoriaUseCase criarCategoriaUseCase,
            final ObterCategoriaPorIdUseCase obterCategoriaPorIdUseCase,
            final AtualizarCategoriaUseCase atualizarCategoriaUseCase,
            final DeletarCategoriaUseCase deletarCategoriaUseCase,
            ListarCategoriaUseCase listarCategoriaUseCase) {
        this.criarCategoriaUseCase = requireNonNull(criarCategoriaUseCase);
        this.obterCategoriaPorIdUseCase = requireNonNull(obterCategoriaPorIdUseCase);
        this.atualizarCategoriaUseCase = requireNonNull(atualizarCategoriaUseCase);
        this.deletarCategoriaUseCase = requireNonNull(deletarCategoriaUseCase);
        this.listarCategoriaUseCase = requireNonNull(listarCategoriaUseCase);
    }

    @Override
    public ResponseEntity<?> criarCategoria(final CriarCategoriaRequest input) {
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
    public Pagination<ListarCategoriaResponse> listarCategorias(
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        SearchQuery searchQuery = new SearchQuery(page, perPage, search, sort, direction);

        return listarCategoriaUseCase.execute(searchQuery)
                .map(CategoriaPresenter::present);
    }

    @Override
    public CategoriaResponse obterPorId(final String id) {
        return CategoriaResponse.from(obterCategoriaPorIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> atualizarPorId(final String id, final AtualizarCategoriaResquest input) {
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
