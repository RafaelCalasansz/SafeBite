DROP TABLE IF EXISTS usuario_restricoes CASCADE;
DROP TABLE IF EXISTS reacoes CASCADE;
DROP TABLE IF EXISTS diario_alimentar CASCADE;
DROP TABLE IF EXISTS carteirinha_emergencia CASCADE;
DROP TABLE IF EXISTS restricoes CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;



CREATE TABLE usuarios (
                          id bigserial PRIMARY KEY,
                          nome varchar(255) NOT NULL,
                          email varchar(255) NOT NULL UNIQUE,
                          password varchar(255) NOT NULL,
                          parent_id bigint,

                          CONSTRAINT fk_parent_user FOREIGN KEY (parent_id) REFERENCES usuarios (id)
);


CREATE TABLE restricoes (
                            id bigserial PRIMARY KEY,
                            nome varchar(255) NOT NULL UNIQUE,
                            descricao text
);


CREATE TABLE carteirinha_emergencia (
                                        user_id bigint PRIMARY KEY,
                                        contatos_emergencia text,
                                        medicamentos_continuos text,
                                        instrucoes_medicas text,

                                        CONSTRAINT fk_user_card FOREIGN KEY (user_id) REFERENCES usuarios (id) ON DELETE CASCADE
);


CREATE TABLE reacoes (
                         id bigserial PRIMARY KEY,
                         alimentos_consumidos varchar(255),
                         intensidade_sintomas varchar(255),
                         medicamentos_utilizados varchar(255),
                         local_ocorrencia varchar(255),
                         data_hora_reacao timestamp,
                         evolucao_quadro text,
                         user_id bigint NOT NULL,

                         CONSTRAINT fk_user_reaction FOREIGN KEY (user_id) REFERENCES usuarios (id)
);


CREATE TABLE diario_alimentar (
                                  id bigserial PRIMARY KEY,
                                  data_hora timestamp,
                                  alimento varchar(255),
                                  observacoes text,
                                  user_id bigint NOT NULL,

                                  CONSTRAINT fk_user_diary FOREIGN KEY (user_id) REFERENCES usuarios (id)
);

CREATE TABLE usuario_restricoes (
                                    user_id bigint NOT NULL,
                                    restriction_id bigint NOT NULL,

                                    PRIMARY KEY (user_id, restriction_id),
                                    CONSTRAINT fk_user_link FOREIGN KEY (user_id) REFERENCES usuarios (id),
                                    CONSTRAINT fk_restriction_link FOREIGN KEY (restriction_id) REFERENCES restricoes (id)
);

CREATE OR REPLACE VIEW view_relatorio_reacoes AS
SELECT
    r.id AS reacao_id,
    u.nome AS nome_paciente,
    u.email AS email_paciente,
    r.alimentos_consumidos,
    r.intensidade_sintomas,
    r.data_hora_reacao,
    r.local_ocorrencia
FROM reacoes r
         JOIN usuarios u ON r.user_id = u.id;



CREATE OR REPLACE FUNCTION fn_contar_reacoes(p_user_id BIGINT)
RETURNS INTEGER AS $$
DECLARE
qtd INTEGER;
BEGIN
SELECT COUNT(*) INTO qtd
FROM reacoes
WHERE user_id = p_user_id;

RETURN qtd;
END;
$$ LANGUAGE plpgsql;




   CREATE OR REPLACE PROCEDURE sp_excluir_reacoes_antigas(p_dias INTEGER)
LANGUAGE plpgsql
AS $$
BEGIN
DELETE FROM reacoes
WHERE data_hora_reacao < NOW() - (p_dias || ' days')::INTERVAL;
END;
$$;