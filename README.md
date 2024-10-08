# sgdrs-backend

## CONEXÃO COM O BD

Configurar as seguintes variáveis de ambiente da IDE para serem usadas conforme o application.yml ao conectar com o BD:
- URL_BD: jdbc:postgresql://localhost:5432/sgdrs
- username: (seu username do Postgre)
- password: (sua senha do Postgre)


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
- Incluir Usuario: POST /auth/basica/cadastrar

Body:


    {
        "nome": "nomeUsuario",
        "email": "email", 
        "senha": "senha", 
        "tipoUsuario": (VOLUNTARIO | ADMIN_CD | ADMIN_ABRIGO | SUPERADMIN)
    }

Retorno:

    {
        "id": "UUID", 
        "nome": "nomeUsuario",
        "email": "email", 
        "tipoUsuario": (VOLUNTARIO | ADMIN_CD | ADMIN_ABRIGO | SUPERADMIN)
    }

- Login: POST /auth/basica/login 

Authentication: 

      {
        "username": "emailUsuario", 
        "password": "senha"
      }

Retorno:

    {
        "id": "UUID", 
        "nome": "nomeUsuario",
        "email": "email", 
        "tipoUsuario": (VOLUNTARIO | ADMIN_CD | ADMIN_ABRIGO | SUPERADMIN)
    }


- Logout: POST /auth/logout

Sem Body

Sem retorno


### Abrigo
- Listar abrigos: GET /abrigos/listar

Sem Body
Retorno:

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


- Criar abrigo: POST /abrigos/criar/{UUID SUPERADMIN}

Body:
    
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

Retorno:
    
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