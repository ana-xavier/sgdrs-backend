package br.com.pucrs.sgdrs.controller;

import br.com.pucrs.sgdrs.controller.response.UsuarioResponse;
import br.com.pucrs.sgdrs.service.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @PostMapping
    public UsuarioResponse login() {
        return usuarioAutenticadoService.getResponse();
    }
}

