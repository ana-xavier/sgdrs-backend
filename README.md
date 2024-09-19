# sgdrs-backend

## Endpoints
> BaseURL: http://localhost:8080/

> Swagger: http://localhost:8080/swagger-ui/index.html

> Actuator: http://localhost:8080/actuator 

### Auth
#### Basic Auth
- Incluir Usuario: POST /auth/basica/cadastrar
> body: {nome: (nomeUsuario) email: (email), senha: (senha), tipoUsuario: (VOLUNTARIO | ADMIN | SUPERADMIN)}

Retorno:
> {id: (UUID), nome: (nomeUsuario) email: (email), tipoUsuario: (VOLUNTARIO | ADMIN | SUPERADMIN)}

- Login: POST /auth/basica/login
> authentication: {username, password}

Retorno:
> {id: (UUID), nome: (nomeUsuario) email: (email), tipoUsuario: (VOLUNTARIO | ADMIN | SUPERADMIN)}


- Logout: POST /auth/logout

Sem body

Sem retorno