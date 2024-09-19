DROP TABLE IF EXISTS doador CASCADE;
DROP TABLE IF EXISTS doacao CASCADE;
DROP TABLE IF EXISTS endereco CASCADE;
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS abrigo CASCADE;
DROP TABLE IF EXISTS centro_distribuicao CASCADE;
DROP TABLE IF EXISTS estoque CASCADE;
DROP TABLE IF EXISTS pedido CASCADE;
DROP TABLE IF EXISTS produto_doacao CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS movimentacao CASCADE;
DROP TABLE IF EXISTS permissao CASCADE;


CREATE TABLE doador (
	id_doador UUID NOT NULL,
	cpf_cnpj VARCHAR(14) NOT NULL,
	nome VARCHAR(250) NOT NULL
);

ALTER TABLE doador ADD CONSTRAINT pk_doador PRIMARY KEY (id_doador);


CREATE TABLE endereco (
	id_endereco UUID NOT NULL,
	cep VARCHAR(250) NOT NULL,
	logradouro VARCHAR(250) NOT NULL,
	numero VARCHAR(250),
	bairro VARCHAR(250) NOT NULL,
	cidade VARCHAR(250) NOT NULL,
	estado VARCHAR(250) NOT NULL
);

ALTER TABLE endereco ADD CONSTRAINT pk_endereco PRIMARY KEY (id_endereco);


CREATE TABLE item (
	id_item UUID NOT NULL,
	nome VARCHAR(250) NOT NULL,
	descricao VARCHAR(250),
	quantidade INTEGER NOT NULL,
	categoria VARCHAR(250) NOT NULL
);

ALTER TABLE item ADD CONSTRAINT pk_item PRIMARY KEY (id_item);


CREATE TABLE abrigo (
	id_abrigo UUID NOT NULL,
	nome VARCHAR(250) NOT NULL,
	id_endereco UUID NOT NULL
);

ALTER TABLE abrigo ADD CONSTRAINT pk_abrigo PRIMARY KEY (id_abrigo);


CREATE TABLE centro_distribuicao (
	id_centro UUID NOT NULL,
	nome VARCHAR(250) NOT NULL,
	id_endereco UUID NOT NULL
);

ALTER TABLE centro_distribuicao ADD CONSTRAINT pk_centro_distribuicao PRIMARY KEY (id_centro);


CREATE TABLE doacao (
	id_doacao UUID NOT NULL,
	data DATE NOT NULL,
	id_doador UUID NOT NULL,
	id_centro UUID NOT NULL
);

ALTER TABLE doacao ADD CONSTRAINT pk_doacao PRIMARY KEY (id_doacao);


CREATE TABLE estoque (
	id_estoque UUID NOT NULL,
	id_centro UUID NOT NULL,
	id_item UUID NOT NULL,
	quantidade INTEGER NOT NULL
);

ALTER TABLE estoque ADD CONSTRAINT pk_estoque PRIMARY KEY (id_estoque);


CREATE TABLE pedido (
	id_pedido UUID NOT NULL,
	data DATE NOT NULL,
	id_abrigo UUID NOT NULL,
	id_centro UUID NOT NULL
);

ALTER TABLE pedido ADD CONSTRAINT pk_pedido PRIMARY KEY (id_pedido);


CREATE TABLE produto_doacao (
	id_produto_doacao UUID NOT NULL,
	id_doacao UUID NOT NULL,
	id_item UUID NOT NULL,
	quantidade INTEGER NOT NULL
);

ALTER TABLE produto_doacao ADD CONSTRAINT pk_produto_doacao PRIMARY KEY (id_produto_doacao);


CREATE TABLE usuario (
	id_usuario UUID NOT NULL,
	nome VARCHAR(250) NOT NULL,
	email VARCHAR(250) NOT NULL,
	senha VARCHAR(250) NOT NULL,
	tipo VARCHAR(250) NOT NULL,
	id_centro UUID,
	id_abrigo UUID,
	ativo	BOOLEAN NOT NULL
);

ALTER TABLE usuario ADD CONSTRAINT pk_usuario PRIMARY KEY (id_usuario);


CREATE TABLE movimentacao (
	id_movimentacao UUID NOT NULL,
	id_item UUID NOT NULL,
	id_pedido UUID NOT NULL,
	quantidade INTEGER NOT NULL
);

ALTER TABLE movimentacao ADD CONSTRAINT pk_movimentacao PRIMARY KEY (id_movimentacao);


CREATE TABLE permissao (
	id 	UUID 			NOT NULL,
	funcao 			VARCHAR(250) 	NOT NULL,
	id_usuario 		UUID 			NOT NULL
);
ALTER TABLE permissao ADD CONSTRAINT pk_permissao PRIMARY KEY (id);
ALTER TABLE permissao ADD CONSTRAINT uk_permissao UNIQUE (funcao, id_usuario);

-- Foreign Key Constraints


ALTER TABLE permissao ADD CONSTRAINT fk_permissao_usuario FOREIGN KEY (id_usuario) REFERENCES usuario;

ALTER TABLE abrigo ADD CONSTRAINT fk_abrigo_endereco FOREIGN KEY (id_endereco) REFERENCES endereco (id_endereco);

ALTER TABLE centro_distribuicao ADD CONSTRAINT fk_centro_distribuicao_endereco FOREIGN KEY (id_endereco) REFERENCES endereco (id_endereco);

ALTER TABLE doacao ADD CONSTRAINT fk_doacao_doador FOREIGN KEY (id_doador) REFERENCES doador (id_doador);
ALTER TABLE doacao ADD CONSTRAINT fk_doacao_centro_distribuicao FOREIGN KEY (id_centro) REFERENCES centro_distribuicao (id_centro);

ALTER TABLE estoque ADD CONSTRAINT fk_estoque_centro_distribuicao FOREIGN KEY (id_centro) REFERENCES centro_distribuicao (id_centro);
ALTER TABLE estoque ADD CONSTRAINT fk_estoque_item FOREIGN KEY (id_item) REFERENCES item (id_item);

ALTER TABLE pedido ADD CONSTRAINT fk_pedido_abrigo FOREIGN KEY (id_abrigo) REFERENCES abrigo (id_abrigo);
ALTER TABLE pedido ADD CONSTRAINT fk_pedido_centro_distribuicao FOREIGN KEY (id_centro) REFERENCES centro_distribuicao (id_centro);

ALTER TABLE produto_doacao ADD CONSTRAINT fk_produto_doacao_doacao FOREIGN KEY (id_doacao) REFERENCES doacao (id_doacao);
ALTER TABLE produto_doacao ADD CONSTRAINT fk_produto_doacao_item FOREIGN KEY (id_item) REFERENCES item (id_item);

ALTER TABLE usuario ADD CONSTRAINT fk_usuario_centro_distribuicao FOREIGN KEY (id_centro) REFERENCES centro_distribuicao (id_centro);
ALTER TABLE usuario ADD CONSTRAINT fk_usuario_abrigo FOREIGN KEY (id_abrigo) REFERENCES abrigo (id_abrigo);

ALTER TABLE movimentacao ADD CONSTRAINT fk_movimentacao_item FOREIGN KEY (id_item) REFERENCES item (id_item);
ALTER TABLE movimentacao ADD CONSTRAINT fk_movimentacao_pedido FOREIGN KEY (id_pedido) REFERENCES pedido (id_pedido);
