package com.catalogo.infrastructure.api;

import com.catalogo.ControllerTest;
import com.catalogo.application.categoria.atualizar.AtualizarCategoriaOutput;
import com.catalogo.application.categoria.atualizar.AtualizarCategoriaUseCase;
import com.catalogo.application.categoria.criar.CriarCategoriaOutput;
import com.catalogo.application.categoria.criar.CriarCategoriaUseCase;
import com.catalogo.application.categoria.deletar.DeletarCategoriaUseCase;
import com.catalogo.application.categoria.recuperar.listar.CategoriaListaOutput;
import com.catalogo.application.categoria.recuperar.listar.ListarCategoriaUseCase;
import com.catalogo.application.categoria.recuperar.obter.CategoriaOutput;
import com.catalogo.application.categoria.recuperar.obter.ObterCategoriaPorIdUseCase;
import com.catalogo.domain.categoria.Categoria;
import com.catalogo.domain.categoria.CategoriaID;
import com.catalogo.domain.exceptions.DomainException;
import com.catalogo.domain.exceptions.NotFoundException;
import com.catalogo.domain.pagination.Pagination;
import com.catalogo.domain.validation.Error;
import com.catalogo.domain.validation.handler.Notification;
import com.catalogo.infrastructure.categoria.models.AtualizarCategoriaResquest;
import com.catalogo.infrastructure.categoria.models.CriarCategoriaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static com.catalogo.domain.categoria.Categoria.newCategoria;
import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoriaAPI.class)
public class CategoriaAPITest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CriarCategoriaUseCase criarCategoriaUseCase;
    @MockBean
    private ObterCategoriaPorIdUseCase obterCategoriaUseCase;
    @MockBean
    private AtualizarCategoriaUseCase atualizarCategoriaUseCase;
    @MockBean
    private DeletarCategoriaUseCase deletarCategoriaUseCase;
    @MockBean
    private ListarCategoriaUseCase listarCategoriaUseCase;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void dadoUmCommandValido_quandoExecutarCriarCategoria_deveRetornaUmaCategoriaComId() throws Exception {
        // given
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;

        final var input =
                new CriarCategoriaRequest(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        when(criarCategoriaUseCase.execute(any()))
                .thenReturn(Right(CriarCategoriaOutput.from("123")));

        //when
        final var request = post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input));

        final var response = mvc.perform(request)
                        .andDo(print());

        // then
        response
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categorias/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo("123")));

        verify(criarCategoriaUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(nomeEsperado, cmd.getNome())
                && Objects.equals(descricaoEsperada, cmd.getDescricao())
                && Objects.equals(isAtivoEsperado, cmd.isAtivo())

        ));

    }

    @Test
    public void dadoUmNomeNullInvalido_quandoExecutarCriarCategoria_deveSerRetornadoNotificationErroStatus422() throws Exception {
        // given
        final String nomeEsperado = null;
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var mensagemErroEsperada = "'nome' não pode ser null";

        final var input =
                new CriarCategoriaRequest(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        when(criarCategoriaUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(mensagemErroEsperada))));
        // when
        final var request = post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        final var response =  mvc.perform(request)
                .andDo(print());


        // then
        response
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo(mensagemErroEsperada)));

        verify(criarCategoriaUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(nomeEsperado, cmd.getNome())
                        && Objects.equals(descricaoEsperada, cmd.getDescricao())
                        && Objects.equals(isAtivoEsperado, cmd.isAtivo())

        ));

    }

    @Test
    public void dadoUmCommandInvalido_quandoExecutarCriarCategoria_deveSerRetornadoDomainException() throws Exception {
        // given
        final String nomeEsperado = null;
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var mensagemErroEsperada = "'nome' não pode ser null";

        final var input =
                new CriarCategoriaRequest(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        when(criarCategoriaUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(mensagemErroEsperada)));

        // when
        final var request = post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        final var response = mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.message", Matchers.equalTo(mensagemErroEsperada)))
                .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo(mensagemErroEsperada)));

        verify(criarCategoriaUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(nomeEsperado, cmd.getNome())
                        && Objects.equals(descricaoEsperada, cmd.getDescricao())
                        && Objects.equals(isAtivoEsperado, cmd.isAtivo())

        ));
    }

    @Test
    public void dadoUmIdValido_quandoChamadoObterCategoriaPorId_entaoRetornaCategoria() throws Exception {
        // given
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var categoria =
                newCategoria(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        final var idEsperado = categoria.getId().getValue();

        when(obterCategoriaUseCase.execute(any()))
                .thenReturn(CategoriaOutput.from(categoria));

        // when

        final var request = get("/categorias/" + idEsperado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(idEsperado)))
                .andExpect(jsonPath("$.nome", Matchers.equalTo(nomeEsperado)))
                .andExpect(jsonPath("$.descricao", Matchers.equalTo(descricaoEsperada)))
                .andExpect(jsonPath("$.is_ativo", Matchers.equalTo(isAtivoEsperado)))
                .andExpect(jsonPath("$.data_criacao", Matchers.equalTo(categoria.getDataCriacao().toString())))
                .andExpect(jsonPath("$.data_atualizacao", Matchers.equalTo(categoria.getDataAtualizacao().toString())))
                .andExpect(jsonPath("$.data_delecao", Matchers.equalTo(categoria.getDataDelecao())));

        verify(obterCategoriaUseCase, times(1)).execute(Mockito.eq(idEsperado));

    }

    @Test
    public void dadoUmIdInvalido_quandoChamadoObterCategoriaPorId_entaoRetornaCategoriaNotFound() throws Exception {
        // given
        final var idEsperado = "123";
        final var mensagemDeErroEsperada = "Categoria com ID 123 não foi encontrado.";

        when(obterCategoriaUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Categoria.class, CategoriaID.from(idEsperado)));

       // when
        final var request = get("/categorias/" + idEsperado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo(mensagemDeErroEsperada)));
    }

    @Test
    public void dadoUmCommandValido_quandoExecutadoAtualizarCategoria_deveRetornaCategoriaId() throws Exception {
        // given
        final var idEsperado = "123";
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;
        final var categoria =
                newCategoria(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        when(atualizarCategoriaUseCase.execute(any()))
                .thenReturn(API.Right(AtualizarCategoriaOutput.from(idEsperado)));

        final var output = new AtualizarCategoriaResquest(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        // when
        final var request = put("/categorias/" + idEsperado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(output));

        final var response = mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(idEsperado)));

        verify(atualizarCategoriaUseCase, times(1)).execute(Mockito.argThat(cmd ->
                    Objects.equals(nomeEsperado, cmd.getNome())
                    && Objects.equals(descricaoEsperada, cmd.getDescricao())
                    && Objects.equals(isAtivoEsperado, cmd.isAtivo())
        ));

    }

    @Test
    public void dadoUmCommandComIdInvalido_quandoExecutarAtualizarCategoria_deveSerRetornadoCategoriaNotFound() throws Exception {
        // given
        final var idEsperado = "not-found";
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;

        final var mensagemDeErroEsperada = "Categoria com ID not-found não foi encontrado.";


        when(atualizarCategoriaUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Categoria.class, CategoriaID.from(idEsperado)));

        final var output = new AtualizarCategoriaResquest(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        // when
        final var request = put("/categorias/" + idEsperado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(output));

        final var response = mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", Matchers.equalTo(mensagemDeErroEsperada)));;

        verify(atualizarCategoriaUseCase, times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(nomeEsperado, cmd.getNome())
                        && Objects.equals(descricaoEsperada, cmd.getDescricao())
                        && Objects.equals(isAtivoEsperado, cmd.isAtivo())
        ));

    }

    @Test
    public void dadoUmNomeInvalido_quandoExecutarAtualizarCategoria_deveSerRetornadoDomainException() throws Exception {
        // given
        final var idEsperado = "123";
        final var nomeEsperado = "Filmes";
        final var descricaoEsperada = "Filmes originais";
        final var isAtivoEsperado = true;

        final var mensagemDeErroEsperada = "'nome' não pode ser null";
        final var qtdDeErroEsperado = 1;


        when(atualizarCategoriaUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(
                        new Error(mensagemDeErroEsperada)
                        ))
                );

        final var output = new AtualizarCategoriaResquest(nomeEsperado, descricaoEsperada, isAtivoEsperado);

        // when
        final var request = put("/categorias/" + idEsperado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(output));

        final var response = mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(qtdDeErroEsperado)))
                .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo(mensagemDeErroEsperada)));

        verify(atualizarCategoriaUseCase, times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(nomeEsperado, cmd.getNome())
                        && Objects.equals(descricaoEsperada, cmd.getDescricao())
                        && Objects.equals(isAtivoEsperado, cmd.isAtivo())
        ));

    }

    @Test
    public void dadoUmIdValido_quandoChamadoDeletarCategoria_entaoDeveRetornarNoContent() throws Exception {
        // given
        final var idEsperado = "123";

        doNothing()
                .when(deletarCategoriaUseCase).execute(any());

        // when
        final var request = delete("/categorias/" + idEsperado)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isNoContent());

        verify(deletarCategoriaUseCase, times(1)).execute(Mockito.eq(idEsperado));
    }

    @Test
    public void dadoParametrosValidos_quandoExecutarListarCategoria_entaoUmaListaDeCategoriasEhRetornada() throws Exception {
        // given
        final var categoria = newCategoria("filmes", null, true);
        final var page = 0;
        final var perPage = 10;
        final var terms = "filmes";
        final var sort = "descricao";
        final var direction = "desc";
        final var total = 1;
        final var qtdItens = 1;
        final var itensEpserados = List.of(CategoriaListaOutput.from(categoria));

        when(listarCategoriaUseCase.execute(any()))
                .thenReturn(new Pagination<>(page, perPage, total, itensEpserados));

        // when
        final var request = get("/categorias")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .queryParam("search", terms)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request)
                .andDo(print());

        //then
         response.andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", Matchers.equalTo(page)))
                .andExpect(jsonPath("$.perPage", Matchers.equalTo(perPage)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(total)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(qtdItens)))
                .andExpect(jsonPath("$.items[0].id", Matchers.equalTo(categoria.getId().getValue())))
                .andExpect(jsonPath("$.items[0].nome", Matchers.equalTo(categoria.getNome())))
                .andExpect(jsonPath("$.items[0].descricao", Matchers.equalTo(categoria.getDescricao())))
                .andExpect(jsonPath("$.items[0].is_ativo", Matchers.equalTo(categoria.isAtivo())))
                .andExpect(jsonPath("$.items[0].data_criacao", Matchers.equalTo(categoria.getDataCriacao().toString())))
                .andExpect(jsonPath("$.items[0].data_delecao", Matchers.equalTo(categoria.getDataDelecao())));

        verify(listarCategoriaUseCase, times(1)).execute(any());
    }

}
