package br.com.sgdrs.service.util;

import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.repository.UsuarioRepository;
import br.com.sgdrs.service.users.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class BuscarUsuarioLogadoService {
    private static final String USUARIO_LOGADO_NAO_ENCONTRADO = "Usuário logado não encontrado";

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getLogado(){
        UUID id =  usuarioAutenticadoService.getId();

        return usuarioRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(NOT_FOUND, USUARIO_LOGADO_NAO_ENCONTRADO));
    }
}
