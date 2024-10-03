package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.service.users.UsuarioAutenticadoService;
import br.com.sgdrs.service.users.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @PostMapping("/auth-basica-login")
    @ResponseStatus(OK)
    public UsuarioResponse login() {
        return usuarioAutenticadoService.getResponse();
    }

    @PostMapping("/cadastrar/{id_criador}")
    @ResponseStatus(CREATED)
    public UsuarioResponse incluir(@Valid @RequestBody IncluirUsuarioRequest request, @PathVariable UUID id_criador) {
        return usuarioService.incluir(request, id_criador);
    }

    @GetMapping("/listar/{tipoFiltrado}")
    public List<UsuarioResponse> listarUsuarios(@PathVariable TipoUsuario tipoFiltrado) {
        return usuarioService.listarUsuarios(tipoFiltrado);
    }

    @PostMapping("/excluir/{idUsuarioSolicitante}/{idUsuarioDeletado}")
    @ResponseStatus(OK)
    public void excluir(@Valid @PathVariable UUID idUsuarioSolicitante, @PathVariable UUID idUsuarioDeletado) {
        usuarioService.excluir(idUsuarioSolicitante, idUsuarioDeletado);
    }
}
