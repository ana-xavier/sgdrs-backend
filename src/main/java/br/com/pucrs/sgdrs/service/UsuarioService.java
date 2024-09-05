package br.com.pucrs.sgdrs.service;

import br.com.pucrs.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.pucrs.sgdrs.controller.response.UsuarioResponse;
import br.com.pucrs.sgdrs.domain.Funcao;
import br.com.pucrs.sgdrs.domain.Permissao;
import br.com.pucrs.sgdrs.domain.Usuario;
import br.com.pucrs.sgdrs.domain.enums.TipoUsuario;
import br.com.pucrs.sgdrs.mapper.UsuarioMapper;
import br.com.pucrs.sgdrs.repository.UsuarioRepository;
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
            case VOLUNTARIO: permissao.setFuncao(Funcao.VOLUNTARIO); break;
            case ADMIN: permissao.setFuncao(Funcao.ADMIN); break;
            case SUPERADMIN: permissao.setFuncao(Funcao.SUPERADMIN); break;
            default: throw new ResponseStatusException(BAD_REQUEST, "Tipo inválido");
        }

        return permissao;
    }
}
