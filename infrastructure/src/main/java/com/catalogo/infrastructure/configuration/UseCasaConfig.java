package com.catalogo.infrastructure.configuration;

import com.catalogo.application.categoria.atualizar.AtualizarCategoriaUseCase;
import com.catalogo.application.categoria.atualizar.DefaultAtualizarCategoriaUseCase;
import com.catalogo.application.categoria.criar.CriarCategoriaUseCase;
import com.catalogo.application.categoria.criar.DefaultCriarCategoriaUseCase;
import com.catalogo.application.categoria.deletar.DefaultDeletarCategoriaUseCase;
import com.catalogo.application.categoria.deletar.DeletarCategoriaUseCase;
import com.catalogo.application.categoria.recuperar.listar.DefaultListarCategoriaUseCase;
import com.catalogo.application.categoria.recuperar.listar.ListarCategoriaUseCase;
import com.catalogo.application.categoria.recuperar.obter.DefaultObterCategoriaPorIdUseCase;
import com.catalogo.application.categoria.recuperar.obter.ObterCategoriaPorIdUseCase;
import com.catalogo.domain.categoria.CategoriaGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasaConfig {

    private CategoriaGateway categoriaGateway;

    public UseCasaConfig(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    @Bean
    public CriarCategoriaUseCase criarCategoriaUseCase() {
        return new DefaultCriarCategoriaUseCase(categoriaGateway);
    }

    @Bean
    public AtualizarCategoriaUseCase atualizarCategoriaUseCase() {
        return new DefaultAtualizarCategoriaUseCase(categoriaGateway);
    }

    @Bean
    public DeletarCategoriaUseCase deletarCategoriaUseCase() {
        return new DefaultDeletarCategoriaUseCase(categoriaGateway);
    }

    @Bean
    public ObterCategoriaPorIdUseCase obterCategoriaPorIdUseCase() {
        return new DefaultObterCategoriaPorIdUseCase(categoriaGateway);
    }

    @Bean
    public ListarCategoriaUseCase listarCategoriaUseCase() {
        return new DefaultListarCategoriaUseCase(categoriaGateway);
    }
}
