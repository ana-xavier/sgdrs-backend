package br.com.sgdrs.service.users;

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
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    public static final String SUPERADMIN_NAO_PODE_CRIAR_VOLUNTARIOS = "Este usuário não tem permissão para criar voluntários!";
    public static final String ADMIN_CD_SO_PODE_CRIAR_VOLUNTARIOS = "Este usuário só pode criar voluntários!";
    public static final String USUARIO_SEM_PERMISSAO_CRIACAO = "Este usuário não tem permissão para criar outros!";
    public static final String USUARIO_COM_EMAIL_EXISTENTE = "O e-mail informado já possui uma conta criada!";
    public static final String USUARIO_CRIADOR_NAO_ENCONTRADO = "Não foi possível encontrar o usuário que está criando!";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponse incluir(IncluirUsuarioRequest request, UUID id_criador) {

        Optional<Usuario> optionalUsuarioCriador = usuarioRepository.findById(id_criador);
        TipoUsuario tipoUsuarioCriador;
        TipoUsuario tipoUsuarioSendoCriado;

        // Verificar que criador existe e criado não
        if (optionalUsuarioCriador.isEmpty()) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, USUARIO_CRIADOR_NAO_ENCONTRADO);
        } else if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST, USUARIO_COM_EMAIL_EXISTENTE);
        }

        Usuario usuarioCriadorEncontrado = optionalUsuarioCriador.get();
        tipoUsuarioCriador = usuarioCriadorEncontrado.getTipo();
        tipoUsuarioSendoCriado = request.getTipo();

        // Validação do tipo de usuário - avaliar se deve ir para método privado
        // futuramente
        switch (tipoUsuarioCriador) {
            case SUPERADMIN: // Só pode criar admins cd ou abrigo
                if (tipoUsuarioSendoCriado.equals(TipoUsuario.VOLUNTARIO)) {
                    throw new ResponseStatusException(FORBIDDEN,
                            SUPERADMIN_NAO_PODE_CRIAR_VOLUNTARIOS);
                }
                break;

            case ADMIN_CD: // Só pode criar voluntários
                if (!tipoUsuarioSendoCriado.equals(TipoUsuario.VOLUNTARIO)) {
                    throw new ResponseStatusException(FORBIDDEN,
                            ADMIN_CD_SO_PODE_CRIAR_VOLUNTARIOS);
                }
                break;

            default:
                // Admins abrigos e voluntários não criam ninguém
                throw new ResponseStatusException(FORBIDDEN, USUARIO_SEM_PERMISSAO_CRIACAO);
        }

        // Se passa aqui, pode criar sem problemas
        Usuario usuarioNovo = UsuarioMapper.toEntity(request);
        usuarioNovo.setSenha(getSenhaCriptografada(request.getSenha()));
        usuarioNovo.adicionarPermissao(getPermissao(request.getTipo()));
        usuarioNovo.setAtivo(true);

        usuarioRepository.save(usuarioNovo);

        return UsuarioMapper.toResponse(usuarioNovo);
    }

    public List<UsuarioResponse> listarUsuarios(TipoUsuario tipoUsuario) {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getTipo().equals(tipoUsuario))
                .map(UsuarioMapper::toResponse)
                .toList();
    }

    private String getSenhaCriptografada(String senhaAberta) {
        return passwordEncoder.encode(senhaAberta);
    }

    private Permissao getPermissao(TipoUsuario tipoUsuario) {
        Permissao permissao = new Permissao();
        switch (tipoUsuario) {
            case VOLUNTARIO:
                permissao.setFuncao(Funcao.VOLUNTARIO);
                break;
            case ADMIN_CD:
                permissao.setFuncao(Funcao.ADMIN_CD);
                break;
            case ADMIN_ABRIGO:
                permissao.setFuncao(Funcao.ADMIN_ABRIGO);
                break;
            case SUPERADMIN:
                permissao.setFuncao(Funcao.SUPERADMIN);
                break;
            default:
                throw new ResponseStatusException(BAD_REQUEST, "Tipo inválido");
        }

        return permissao;
    }
}
