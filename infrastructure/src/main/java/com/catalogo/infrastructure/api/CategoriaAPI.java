package com.catalogo.infrastructure.api;

import com.catalogo.domain.pagination.Pagination;
import com.catalogo.infrastructure.categoria.models.AtualizarCategoriaAPIInput;
import com.catalogo.infrastructure.categoria.models.CategoriaAPIOutput;
import com.catalogo.infrastructure.categoria.models.CriarCategoriaAPIInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "categorias")
@Tag(name = "Categorias")
public interface CategoriaAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Criar uma nova categoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "201", description = "Criado com sucesso."),
            @ApiResponse(responseCode =  "422", description = "Um erro de validação foi lançado."),
            @ApiResponse(responseCode =  "500", description = "Um erro interno foi lançado.")
    })
    ResponseEntity<?> criarCategoria(@RequestBody CriarCategoriaAPIInput input);

    @GetMapping
    @Operation(summary = "Listar todas as categorias paginadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "200", description = "Listado com sucesso."),
            @ApiResponse(responseCode =  "422", description = "Um parametro inválido foi recebido."),
            @ApiResponse(responseCode =  "500", description = "Um erro interno foi lançado.")
    })
    Pagination<?> listarCategorias(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "nome") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Obter uma categoria por identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "200", description = "Categoria recuperada com sucesso."),
            @ApiResponse(responseCode =  "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode =  "500", description = "Um erro interno foi lançado.")
    })
    CategoriaAPIOutput obterPorId(@PathVariable("id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Atualizar uma categoria por identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "200", description = "Categoria atualizada com sucesso."),
            @ApiResponse(responseCode =  "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode =  "500", description = "Um erro interno foi lançado.")
    })
    ResponseEntity<?> atualizarPorId(@PathVariable("id") String id, @RequestBody AtualizarCategoriaAPIInput input);

    @DeleteMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar uma categoria por identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "200", description = "Categoria deletada com sucesso."),
            @ApiResponse(responseCode =  "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode =  "500", description = "Um erro interno foi lançado.")
    })
    void deletarPorId(@PathVariable("id") String id);


}
