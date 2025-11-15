/*
 * SCRIPT DML (Data Manipulation Language)
 * ---------------------------------------
 * Este script insere os dados iniciais necessários
 * para o funcionamento do sistema SafeBite.
 */

-- Popula a tabela de Restrições Padrão
INSERT INTO restricoes (nome, descricao)
VALUES
    ('Glúten', 'Proteína encontrada no trigo, cevada e centeio.'),
    ('Lactose', 'Açúcar encontrado no leite e laticínios.'),
    ('Amendoim', 'Leguminosa que causa reações alérgicas.'),
    ('Frutos do Mar', 'Inclui peixes, crustáceos e moluscos.'),
    ('Soja', 'Leguminosa comum em muitos alimentos processados.'),
    ('Ovos', 'Proteína presente na clara e gema do ovo.'),
    ('Frutos Secos', 'Inclui nozes, amêndoas, avelãs, etc.');

/*
 * NOTA:
 * Os utilizadores (INSERT INTO usuarios...) não são adicionados aqui,
 * pois são criados dinamicamente através da tela de Registo da aplicação.
 * A senha no banco é sempre criptografada (BCrypt).
 */