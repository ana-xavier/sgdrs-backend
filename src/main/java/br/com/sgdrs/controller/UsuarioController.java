package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.service.users.UsuarioAutenticadoService;
import br.com.sgdrs.service.users.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
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

    @RolesAllowed({"SUPERADMIN", "ADMIN_CD"})
    @PostMapping("/cadastrar")
    @ResponseStatus(CREATED)
    public UsuarioResponse incluir(@Valid @RequestBody IncluirUsuarioRequest request) {
        return usuarioService.incluir(request);
    }

    @RolesAllowed({"SUPERADMIN"})
    @GetMapping("/listar/{tipoFiltrado}")
    public List<UsuarioResponse> listarUsuarios(@PathVariable TipoUsuario tipoFiltrado) {
        return usuarioService.listarUsuarios(tipoFiltrado);
    }

    @RolesAllowed({"ADMIN_CD"})
    @GetMapping("/listarVoluntarios")
    public List<UsuarioResponse> listarVoluntarios(@RequestParam(required = false) String nome ) {
        return usuarioService.listarVoluntarios(nome);
    }

    @RolesAllowed({"SUPERADMIN", "ADMIN_CD"})
    @DeleteMapping("/excluir/{idUsuarioDeletado}")
    @ResponseStatus(OK)
    public void excluir(@Valid @PathVariable UUID idUsuarioDeletado) {
        usuarioService.excluir(idUsuarioDeletado);
    }

    @RolesAllowed({"SUPERADMIN"})
    @GetMapping("/detalhar/{idUsuario}")
    @ResponseStatus(OK)
    public UsuarioResponse buscarInformacoesUsuario(@PathVariable UUID idUsuario){
        return usuarioService.buscarInformacoesUsuario(idUsuario);
    }
}
