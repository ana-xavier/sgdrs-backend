package br.com.pucrs.sgdrs.controller;

import br.com.pucrs.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.pucrs.sgdrs.controller.response.UsuarioResponse;
import br.com.pucrs.sgdrs.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/usuariosRegistro")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping
    @ResponseStatus(CREATED)
    public UsuarioResponse incluir(@RequestBody IncluirUsuarioRequest request){
        return usuarioService.incluir(request);
    }
}
