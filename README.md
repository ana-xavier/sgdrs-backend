# sgdrs-backend

## Endpoints
> BaseURL: http://localhost:8080/

### Auth
#### Basic Auth
- Incluir Usuario: POST /usuarioRegistro
> body: {username: (email), password: (senha)}

- Login: POST /login
> authentication: {username, password}

- Logout: POST /logout

- teste: GET /testeLogout
> apenas para testar se fica privado sem o usuário estar logado