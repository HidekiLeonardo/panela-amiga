ALTER TABLE tb_receitas
ADD usuario_id BIGINT NOT NULL REFERENCES tb_usuarios(id);

ALTER TABLE tb_ingredientes
ADD usuario_id BIGINT NOT NULL REFERENCES tb_usuarios(id);

ALTER TABLE tb_transacoes_financeiras
ADD usuario_id BIGINT NOT NULL REFERENCES tb_usuarios(id);