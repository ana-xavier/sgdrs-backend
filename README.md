# sgdrs-backend

## CONEXÃO COM O BD

Configurar as seguintes variáveis de ambiente da IDE para serem usadas conforme o application.yml ao conectar com o BD:
- URL_BD: jdbc:postgresql://localhost:5432/sgdrs
- username: (seu username do Postgre)
- password: (sua senha do Postgre)

## ACESSOS:

| Tipo         | Username                 | Senha          |
|--------------|--------------------------|----------------|
| VOLUNTARIO   | voluntario1@exemplo.com  | senhaSegura123 |
| VOLUNTARIO   | voluntario2@exemplo.com  | senhaSegura123 |
| VOLUNTARIO   | voluntario3@exemplo.com  | senhaSegura123 |
| VOLUNTARIO   | voluntario4@exemplo.com  | senhaSegura123 |
| VOLUNTARIO   | voluntario5@exemplo.com  | senhaSegura123 |
| ADMIN_CD     | admin_cd@exemplo.com     | senhaAdmin123  |
| ADMIN_ABRIGO | admin_abrigo@exemplo.com | senhaAdmin123  |
| SUPERADMIN   | sadm@email.com           | 1234           |


## GERAÇÃO DE SENHAS HASH EM BCRYPT
Utilizar o site https://bcrypt-generator.com/

!!!!! **DEFINIR ROUNDS = 12** !!!!!

## GERAÇÃO DE UUIDs
Utilizar o site https://www.uuidgenerator.net/version4
- Definir no menu abaixo a quantidade de UUIDs desejada e clicar em "Generate"

-----

## Endpoints

> BaseURL: http://localhost:8080/

> Swagger: http://localhost:8080/swagger-ui/index.html

> Actuator: http://localhost:8080/actuator 
 

## JSON de erro padrão
    {
        "erro": "(Tipo erro)",
        "mensagem": "(Mensagem erro)",
        "status": "(Código do erro)"
    }

- Exemplo:
    

    {
        "erro": "Bad Request",
        "mensagem": "Senha não pode ser vazia",
        "status": "400"
    }


### Auth
#### Basic Auth
- Incluir Usuario: **POST** /auth/basica/cadastrar

**Body**:
```
    {
        "nome": "nomeUsuario",
        "email": "email", 
        "senha": "senha", 
        "tipoUsuario": (VOLUNTARIO | ADMIN_CD | ADMIN_ABRIGO | SUPERADMIN)
    }
```

**Retorno**:
```
    {
        "id": "UUID", 
        "nome": "nomeUsuario",
        "email": "email", 
        "tipoUsuario": (VOLUNTARIO | ADMIN_CD | ADMIN_ABRIGO | SUPERADMIN)
    }
```

- Login: **POST** /auth/basica/login 

**Authentication**: 
```
      {
        "username": "emailUsuario", 
        "password": "senha"
      }
```
**Retorno**:
```
    {
        "id": "UUID", 
        "nome": "nomeUsuario",
        "email": "email", 
        "tipoUsuario": (VOLUNTARIO | ADMIN_CD | ADMIN_ABRIGO | SUPERADMIN)
    }
```

- Logout: **POST** /auth/logout

>Sem **Body**
>Sem **Retorno**


### Usuário
- Listar informações de um usuário específico: **GET** usuarios/usuario/{idUsuarioSolicitante}/{idUsuario}

>Sem **Body**

**Retorno**:

```
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "email": "string",
  "nome": "string",
  "tipo": "VOLUNTARIO",
  "id_abrigo": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "id_centroDistribuicao": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "ativo": true
}
```

- Cadastrar um usuário: **POST** usuarios/cadastrar/{id_criador}
*É importante ressaltar que o usuário só poderá ser criado se o id_criador atender a todas as regras de negócio estabelecidas*.

**Body**
```
{
  "nome": "RdzSIUpdPthScoCj TwEtnykvVwplKEQvEBbMniEvUwXRpiItkNZQnHtsL vZHzpOigBpZLdjoLBOZcsFpFBSbYnmSCYN",
  "email": "string",
  "tipo": "VOLUNTARIO"
}
```
*`nome` é uma expressão regular em cima de uma String: regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "Nome deve conter apenas letras e espaços", onde também não pode ser vazio ou conter mais de 250 caracteres.*
**A senha, conforme estabelecido, será criada aleatoriamente no momento do cadastro por um superior, devendo ser alterada depois (ainda não há).**

**Retorno**:
```
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "email": "string",
  "nome": "string",
  "tipo": "VOLUNTARIO",
  "id_abrigo": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "id_centroDistribuicao": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "ativo": true
}
```

- Listar usuários filtrados por função: **GET** "usuarios/listar/{tipoFiltrado}"

>Sem **Body**

**Retorno:**
```
[
  {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "email": "string",
    "nome": "string",
    "tipo": "VOLUNTARIO",
    "id_abrigo": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "id_centroDistribuicao": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "ativo": true
  }
]
```

- Excluir (desativar) um usuário: **DELETE** "usuarios/excluir/{idUsuarioSolicitante}/{idUsuarioDeletado}"

>Sem **Body**

**Retorno**: HTTP_STATUS(OK) - Código 200
*O usuário não é deletado de fato, mas tem seu atributo `ativo` definido como `false`, sendo excluído de futuras listagens.*


### Abrigo
- Listar abrigos: **GET** /abrigos/listar

>Sem **Body**

**Retorno**:
```
    {
        [

            {
                "id": "idAbrigo",
                "nome": "nomeAbrigo",
                "endereco": {
                    "cep": "cep",
                    "logradouro": "logradouro",
                    "numero": "numero",
                    "bairro": "bairro",
                    "cidade": "cidade",
                    "estado": "siglaEstado"
                }
            },
            ...
        ]
    }
```

- Criar abrigo: **POST** /abrigos/criar/{UUID SUPERADMIN}

**Body**:
 ```   
    {
        "nome": "nomeAbrigo",
        "endereco": {
            "cep": "cep",
            "logradouro": "logradouro",
            "numero": "numero",
            "bairro": "bairro",
            "cidade": "cidade",
            "estado": "siglaEstado"
    }
```
**Retorno**:
```    
    {
        "id": "UUID Abrigo",
        "nome": "nomeAbrigo",
        "endereco": {
            "cep": "cep",
            "logradouro": "logradouro",
            "numero": "numero",
            "bairro": "bairro",
            "cidade": "cidade",
            "estado": "siglaEstado"
    }
```