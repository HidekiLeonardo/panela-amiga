CREATE TABLE tb_ingredientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    unidade_medida VARCHAR(50) NOT NULL,
    marca VARCHAR(255) NOT NULL,
    fornecedor VARCHAR(255),
    custo_unitario NUMERIC(10,2),
    quantidade_em_estoque NUMERIC(10,2),
    data_validade DATE NOT NULL,
    data_atualizacao TIMESTAMP
);

CREATE TABLE tb_receitas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    modo_preparo TEXT NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    tempo_preparo INT NOT NULL,
    porcoes INT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL,
    rendimento DOUBLE PRECISION NOT NULL,
    custo_total NUMERIC(10,2) NOT NULL,
    preco_venda NUMERIC(10,2) NOT NULL
);

CREATE TABLE ingrediente_receita (
    id BIGSERIAL PRIMARY KEY,
    receita_id BIGINT NOT NULL REFERENCES tb_receitas(id),
    ingrediente_id BIGINT NOT NULL REFERENCES tb_ingredientes(id),
    quantidade NUMERIC(10,2)
);

CREATE TABLE tb_transacoes_financeiras (
    id BIGSERIAL PRIMARY KEY,
    tipo_transacao VARCHAR(50) NOT NULL,
    descricao VARCHAR(50),
    valor NUMERIC(10,2) NOT NULL,
    data DATE NOT NULL,
    pagamento VARCHAR(50) NOT NULL,
    origem_transacao VARCHAR(50) NOT NULL,
    receita_id BIGINT REFERENCES tb_receitas(id)
);