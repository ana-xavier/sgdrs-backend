# sgdrs-backend

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
body:


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
authentication: 

      {
        username, 
        password
      }

Retorno:

    {
        "id": "UUID", 
        "nome": "nomeUsuario",
        "email": "email", 
        "tipoUsuario": (VOLUNTARIO | ADMIN_CD | ADMIN_ABRIGO | SUPERADMIN)
    }


- Logout: POST /auth/logout

Sem body

Sem retorno


### Abrigo
- Listar abrigos: GET /abrigos/listar

Sem body
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