DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS permissao CASCADE;

CREATE TABLE usuario(
	id		UUID			NOT NULL,
	nome	VARCHAR(100)	NOT NULL,
	email	VARCHAR(100)	NOT NULL,
	senha	VARCHAR(80)		NOT NULL,
	tipo	VARCHAR(20)		NOT NULL,
	ativo	BOOLEAN			NOT NULL,

	CONSTRAINT ck_usuario_tipo CHECK(tipo IN ('SUPERADMIN','ADMIN','VOLUNTARIO')),
	CONSTRAINT pk_usuario PRIMARY KEY (id),
	CONSTRAINT uk_usuario_email UNIQUE (email)
	
);

CREATE TABLE permissao (
	id 			UUID 			NOT NULL,
	funcao 		VARCHAR(100) 	NOT NULL,
	usuario_id 	UUID 			NOT NULL
);
ALTER TABLE permissao ADD CONSTRAINT pk_permissao PRIMARY KEY (id);
ALTER TABLE permissao ADD CONSTRAINT uk_permissao UNIQUE (funcao, usuario_id);
ALTER TABLE permissao ADD CONSTRAINT fk_permissao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario;



select * from usuario