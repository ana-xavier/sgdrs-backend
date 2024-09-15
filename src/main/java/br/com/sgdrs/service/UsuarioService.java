package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.Funcao;
import br.com.sgdrs.domain.Permissao;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.mapper.UsuarioMapper;
import br.com.sgdrs.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class UsuarioService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponse incluir(IncluirUsuarioRequest request) {

        Usuario usuario = UsuarioMapper.toEntity(request);
        usuario.setSenha(getSenhaCriptografada(request.getSenha()));
        usuario.adicionarPermissao(getPermissao(request.getTipoUsuario()));
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        return UsuarioMapper.toResponse(usuario);
    }

    private String getSenhaCriptografada(String senhaAberta) {
        return passwordEncoder.encode(senhaAberta);
    }

    private Permissao getPermissao(TipoUsuario tipoUsuario) {
        Permissao permissao = new Permissao();
        switch (tipoUsuario){
            case volunteer: permissao.setFuncao(Funcao.VOLUNTARIO); break;
            case admin: permissao.setFuncao(Funcao.ADMIN); break;
            case superadmin: permissao.setFuncao(Funcao.SUPERADMIN); break;
            default: throw new ResponseStatusException(BAD_REQUEST, "Tipo inválido");
        }

        return permissao;
    }
}
