TRUNCATE doador CASCADE;
TRUNCATE endereco CASCADE;
TRUNCATE item CASCADE;
TRUNCATE abrigo CASCADE;
TRUNCATE centro_distribuicao CASCADE;
TRUNCATE usuario CASCADE;
TRUNCATE doacao CASCADE;
TRUNCATE pedido CASCADE;
TRUNCATE movimentacao CASCADE;
TRUNCATE produto_doacao CASCADE;
TRUNCATE permissao CASCADE;

INSERT INTO doador(id_doador, cpf_cnpj, nome) VALUES
    (gen_random_uuid(), '12345678901', 'João Silva'),
    (gen_random_uuid(), '98765432100', 'Maria Oliveira'),
    (gen_random_uuid(), '45678912312', 'Carlos Souza'),
    (gen_random_uuid(), '78912345678', 'Ana Costa'),
    (gen_random_uuid(), '32165498709', 'Fernanda Lima');

INSERT INTO endereco(id_endereco, cep, logradouro, numero, bairro, cidade, estado) VALUES
    (gen_random_uuid(), '01001000', 'Praça da Sé', '1', 'Sé', 'São Paulo', 'SP'),
    (gen_random_uuid(), '20040000', 'Avenida Presidente Vargas', '233', 'Centro', 'Rio de Janeiro', 'RJ'),
    (gen_random_uuid(), '30130100', 'Rua da Bahia', '345', 'Centro', 'Belo Horizonte', 'MG'),
    (gen_random_uuid(), '40010000', 'Rua Chile', '89', 'Comércio', 'Salvador', 'BA'),
    (gen_random_uuid(), '50050000', 'Avenida Boa Viagem', '900', 'Boa Viagem', 'Recife', 'PE');


INSERT INTO abrigo(id_abrigo, nome, id_endereco) VALUES
    (gen_random_uuid(), 'Abrigo Esperança', (SELECT id_endereco FROM endereco WHERE logradouro = 'Praça da Sé')),
    (gen_random_uuid(), 'Abrigo Luz do Sol', (SELECT id_endereco FROM endereco WHERE logradouro = 'Avenida Presidente Vargas')),
    (gen_random_uuid(), 'Abrigo Vida Nova', (SELECT id_endereco FROM endereco WHERE logradouro = 'Rua da Bahia')),
    (gen_random_uuid(), 'Abrigo Paz e Bem', (SELECT id_endereco FROM endereco WHERE logradouro = 'Rua Chile')),
    (gen_random_uuid(), 'Abrigo Alegria', (SELECT id_endereco FROM endereco WHERE logradouro = 'Avenida Boa Viagem'));

INSERT INTO centro_distribuicao(id_centro, nome, id_endereco) VALUES
    (gen_random_uuid(), 'Centro São Paulo', (SELECT id_endereco FROM endereco WHERE logradouro = 'Praça da Sé')),
    (gen_random_uuid(), 'Centro Rio de Janeiro', (SELECT id_endereco FROM endereco WHERE logradouro = 'Avenida Presidente Vargas')),
    (gen_random_uuid(), 'Centro Belo Horizonte', (SELECT id_endereco FROM endereco WHERE logradouro = 'Rua da Bahia')),
    (gen_random_uuid(), 'Centro Salvador', (SELECT id_endereco FROM endereco WHERE logradouro = 'Rua Chile')),
    (gen_random_uuid(), 'Centro Recife', (SELECT id_endereco FROM endereco WHERE logradouro = 'Avenida Boa Viagem'));

INSERT INTO item(id_item, nome, descricao, quantidade, categoria, id_centro, cod_barras, validado) VALUES
    (gen_random_uuid(), 'Arroz', 'Pacote de arroz 5kg', 100, 'Alimento', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro São Paulo'), '123456789012', true),
    (gen_random_uuid(), 'Feijão', 'Pacote de feijão 1kg', 200, 'Alimento', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Rio de Janeiro'), '234567890123', true),
    (gen_random_uuid(), 'Cobertor', 'Cobertor de lã', 50, 'Vestuário', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Belo Horizonte'), '345678901234', true),
    (gen_random_uuid(), 'Camiseta', 'Camiseta de algodão', 150, 'Vestuário', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Salvador'), '456789012345', true),
    (gen_random_uuid(), 'Sapato', 'Sapato de couro', 75, 'Calçados', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Recife'), '567890123456', true);

INSERT INTO usuario(id_usuario, email, nome, senha, tipo, id_centro, id_abrigo, ativo) VALUES
    -- Voluntários (id_centro NÃO NULO, id_abrigo NULO)
    (gen_random_uuid(), 'voluntario1@exemplo.com', 'Voluntario 1', '$2a$12$WB4R5kKc3veC9uJNNndRbe5/kreCZTRnsvSTMiQAybExipt.RVeBK', 'VOLUNTARIO', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro São Paulo'), NULL, TRUE),
    (gen_random_uuid(), 'voluntario2@exemplo.com', 'Voluntario 2', '$2a$12$WB4R5kKc3veC9uJNNndRbe5/kreCZTRnsvSTMiQAybExipt.RVeBK', 'VOLUNTARIO', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Rio de Janeiro'), NULL, TRUE),
    (gen_random_uuid(), 'voluntario3@exemplo.com', 'Voluntario 3', '$2a$12$WB4R5kKc3veC9uJNNndRbe5/kreCZTRnsvSTMiQAybExipt.RVeBK', 'VOLUNTARIO', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Belo Horizonte'), NULL, TRUE),
    (gen_random_uuid(), 'voluntario4@exemplo.com', 'Voluntario 4', '$2a$12$WB4R5kKc3veC9uJNNndRbe5/kreCZTRnsvSTMiQAybExipt.RVeBK', 'VOLUNTARIO', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Salvador'), NULL, TRUE),
    (gen_random_uuid(), 'voluntario5@exemplo.com', 'Voluntario 5', '$2a$12$WB4R5kKc3veC9uJNNndRbe5/kreCZTRnsvSTMiQAybExipt.RVeBK', 'VOLUNTARIO', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Recife'), NULL, TRUE),
    
    -- Admin Centro de Distribuição (id_centro NÃO NULO, id_abrigo NULO)
    (gen_random_uuid(), 'admin_cd@exemplo.com', 'Admin CD', '$2a$12$.MU2wRbQDTJ10Z9tlXGJpeTXvZnihJcSO59SnAB7nmoXmXk7SPZBe', 'ADMIN_CD', (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro São Paulo'), NULL, TRUE),
    
    -- Admin Abrigo (id_centro NULO, id_abrigo NÃO NULO)
    (gen_random_uuid(), 'admin_abrigo@exemplo.com', 'Admin Abrigo', '$2a$12$.MU2wRbQDTJ10Z9tlXGJpeTXvZnihJcSO59SnAB7nmoXmXk7SPZBe', 'ADMIN_ABRIGO', NULL, (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Esperança'), TRUE);

	 -- SUPERADMIN (id_centro NULO, id_abrigo NULO)
    -- (gen_random_uuid(), 'sadm@email.com', 'Superadmin', '$2a$12$incRGiD/U5gBfjdfxPfmnuUB7P6C0MCUyw5wEWA9aTfRjXuJGZ4QW', 'SUPERADMIN', NULL, NULL, TRUE);

INSERT INTO permissao(id, funcao, id_usuario) VALUES
	(gen_random_uuid(), 'ROLE_VOLUNTARIO',(SELECT id_usuario FROM usuario where nome = 'Voluntario 1')),
	(gen_random_uuid(), 'ROLE_VOLUNTARIO',(SELECT id_usuario FROM usuario where nome = 'Voluntario 2')),
	(gen_random_uuid(), 'ROLE_VOLUNTARIO',(SELECT id_usuario FROM usuario where nome = 'Voluntario 3')),
	(gen_random_uuid(), 'ROLE_VOLUNTARIO',(SELECT id_usuario FROM usuario where nome = 'Voluntario 4')),
	(gen_random_uuid(), 'ROLE_VOLUNTARIO',(SELECT id_usuario FROM usuario where nome = 'Voluntario 5')),
	(gen_random_uuid(), 'ROLE_ADMIN_CD',(SELECT id_usuario FROM usuario where nome = 'Admin CD')),
	(gen_random_uuid(), 'ROLE_ADMIN_ABRIGO',(SELECT id_usuario FROM usuario where nome = 'Admin Abrigo'));
	-- (gen_random_uuid(), 'ROLE_SUPERADMIN',(SELECT id_usuario FROM usuario where nome = 'Superadmin'));
	

INSERT INTO doacao(id_doacao, data, id_doador, id_centro) VALUES
    (gen_random_uuid(), '2024-10-08', (SELECT id_doador FROM doador WHERE nome = 'João Silva'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro São Paulo')),
    (gen_random_uuid(), '2024-10-08', (SELECT id_doador FROM doador WHERE nome = 'Maria Oliveira'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Rio de Janeiro')),
    (gen_random_uuid(), '2024-10-08', (SELECT id_doador FROM doador WHERE nome = 'Carlos Souza'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Belo Horizonte')),
    (gen_random_uuid(), '2024-10-08', (SELECT id_doador FROM doador WHERE nome = 'Ana Costa'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Salvador')),
    (gen_random_uuid(), '2024-10-08', (SELECT id_doador FROM doador WHERE nome = 'Fernanda Lima'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Recife'));


INSERT INTO pedido(id_pedido, data, id_abrigo, id_centro, id_voluntario, status) VALUES
    (gen_random_uuid(), '2024-10-08', (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Esperança'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro São Paulo'), (SELECT id_usuario FROM usuario WHERE nome = 'Voluntario 1'), 'EM_PREPARO'),
    (gen_random_uuid(), '2024-10-08', (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Luz do Sol'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Rio de Janeiro'), (SELECT id_usuario FROM usuario WHERE nome = 'Voluntario 2'), 'EM_PREPARO'),
    (gen_random_uuid(), '2024-10-08', (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Vida Nova'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Belo Horizonte'), (SELECT id_usuario FROM usuario WHERE nome = 'Voluntario 3'), 'EM_PREPARO'),
    (gen_random_uuid(), '2024-10-08', (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Paz e Bem'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Salvador'), (SELECT id_usuario FROM usuario WHERE nome = 'Voluntario 4'), 'EM_PREPARO'),
    (gen_random_uuid(), '2024-10-08', (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Alegria'), (SELECT id_centro FROM centro_distribuicao WHERE nome = 'Centro Recife'), (SELECT id_usuario FROM usuario WHERE nome = 'Voluntario 5'), 'EM_PREPARO');

INSERT INTO produto_doacao(id_produto_doacao, id_doacao, id_item, quantidade) VALUES
    (gen_random_uuid(), (SELECT id_doacao FROM doacao WHERE id_doador = (SELECT id_doador FROM doador WHERE nome = 'Maria Oliveira')), (SELECT id_item FROM item WHERE nome = 'Arroz'), 20),
    (gen_random_uuid(), (SELECT id_doacao FROM doacao WHERE id_doador = (SELECT id_doador FROM doador WHERE nome = 'Maria Oliveira')), (SELECT id_item FROM item WHERE nome = 'Feijão'), 50),
    (gen_random_uuid(), (SELECT id_doacao FROM doacao WHERE id_doador = (SELECT id_doador FROM doador WHERE nome = 'Carlos Souza')), (SELECT id_item FROM item WHERE nome = 'Cobertor'), 15),
    (gen_random_uuid(), (SELECT id_doacao FROM doacao WHERE id_doador = (SELECT id_doador FROM doador WHERE nome = 'Ana Costa')), (SELECT id_item FROM item WHERE nome = 'Camiseta'), 25),
    (gen_random_uuid(), (SELECT id_doacao FROM doacao WHERE id_doador = (SELECT id_doador FROM doador WHERE nome = 'Fernanda Lima')), (SELECT id_item FROM item WHERE nome = 'Sapato'), 10);

INSERT INTO movimentacao(id_movimentacao, id_item, id_pedido, quantidade) VALUES
    (gen_random_uuid(), (SELECT id_item FROM item WHERE nome = 'Arroz'), (SELECT id_pedido FROM pedido WHERE id_abrigo = (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Esperança')), 10),
    (gen_random_uuid(), (SELECT id_item FROM item WHERE nome = 'Feijão'), (SELECT id_pedido FROM pedido WHERE id_abrigo = (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Luz do Sol')), 20),
    (gen_random_uuid(), (SELECT id_item FROM item WHERE nome = 'Cobertor'), (SELECT id_pedido FROM pedido WHERE id_abrigo = (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Vida Nova')), 5),
    (gen_random_uuid(), (SELECT id_item FROM item WHERE nome = 'Camiseta'), (SELECT id_pedido FROM pedido WHERE id_abrigo = (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Paz e Bem')), 10),
    (gen_random_uuid(), (SELECT id_item FROM item WHERE nome = 'Sapato'), (SELECT id_pedido FROM pedido WHERE id_abrigo = (SELECT id_abrigo FROM abrigo WHERE nome = 'Abrigo Alegria')), 8);