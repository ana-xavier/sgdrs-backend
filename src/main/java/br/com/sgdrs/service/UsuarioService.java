package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.enums.Funcao;
import br.com.sgdrs.domain.Permissao;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.mapper.UsuarioMapper;
import br.com.sgdrs.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponse incluir(IncluirUsuarioRequest request) {
        Usuario usuario = UsuarioMapper.toEntity(request);
        usuario.setSenha(getSenhaCriptografada(request.getSenha()));
        usuario.adicionarPermissao(getPermissao(request.getTipo()));
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
            case ADMIN_CD: permissao.setFuncao(Funcao.ADMIN_CD); break;
            case ADMIN_ABRIGO: permissao.setFuncao(Funcao.ADMIN_ABRIGO); break;
            case SUPERADMIN: permissao.setFuncao(Funcao.SUPERADMIN); break;
            default: throw new ResponseStatusException(BAD_REQUEST, "Tipo inválido");
        }

        return permissao;
    }
}
