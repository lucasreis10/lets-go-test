CREATE TABLE categoria (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(4000),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao DATETIME(6) NOT NULL,
    data_atualizacao DATETIME(6) NOT NULL,
    data_delecao DATETIME(6) NULL
);