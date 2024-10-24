package br.com.sgdrs.service.users;

import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.UsuarioSecurity;
import br.com.sgdrs.repository.UsuarioRepository;
import br.com.sgdrs.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class UsuarioAutenticadoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UUID getId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UsuarioSecurity) {
            return ((UsuarioSecurity) authentication.getPrincipal()).getId();
        }

        return null;
    }

    public UsuarioSecurity getUsuarioLogado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UsuarioSecurity) {
            return ((UsuarioSecurity) authentication.getPrincipal());
        }

        return null;
    }

    public Usuario get() {
        UUID id = getId();

        if (isNull(id)) {
            return null;
        }

        return usuarioRepository.findById(getId()).orElse(null);
    }

    public UsuarioResponse getResponse() {
        Usuario entity = get();
        System.out.println("entity");
        System.out.println(entity);

        return nonNull(entity) ? UsuarioMapper.toResponse(entity) : new UsuarioResponse();
    }
}
