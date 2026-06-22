CREATE TABLE tb_movimentacao_estoque (
    id BIGSERIAL PRIMARY KEY,
    ingrediente_id BIGINT NOT NULL REFERENCES tb_ingredientes(id),
    tipo VARCHAR(50) NOT NULL,
    quantidade NUMERIC(10,2) NOT NULL,
    data TIMESTAMP NOT NULL,
    usuario_id BIGINT NOT NULL REFERENCES tb_usuarios(id)
);