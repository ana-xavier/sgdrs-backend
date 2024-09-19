package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.service.UsuarioAutenticadoService;
import br.com.sgdrs.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth/basica")
public class AuthBasicaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @PostMapping("/login")
    @ResponseStatus(OK)
    public UsuarioResponse login() {
        return usuarioAutenticadoService.getResponse();
    }

    @PostMapping("/cadastrar")
    @ResponseStatus(CREATED)
    public UsuarioResponse incluir(@RequestBody IncluirUsuarioRequest request){
        return usuarioService.incluir(request);
    }
}
