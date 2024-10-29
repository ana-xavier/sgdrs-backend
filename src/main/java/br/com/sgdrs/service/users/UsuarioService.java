package br.com.sgdrs.service.users;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.enums.Funcao;
import br.com.sgdrs.domain.Permissao;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.mapper.UsuarioMapper;
import br.com.sgdrs.repository.PermissaoRepository;
import br.com.sgdrs.repository.UsuarioRepository;
import br.com.sgdrs.service.util.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Service
public class UsuarioService {

    private static final String SUPERADMIN_NAO_PODE_CRIAR_VOLUNTARIOS = "Este usuário não tem permissão para criar voluntários!";
    private static final String ADMIN_CD_SO_PODE_CRIAR_E_DELETAR_VOLUNTARIOS = "Este usuário só pode criar e deletar voluntários!";
    private static final String USUARIO_SEM_PERMISSAO_CRIACAO_DELECAO = "Este usuário não tem permissão para criar ou deletar outros usuários!";
    private static final String USUARIO_COM_EMAIL_EXISTENTE = "O e-mail informado já possui uma conta criada!";
    private static final String USUARIO_SOLICITANTE_NAO_ENCONTRADO = "Não foi possível encontrar o usuário que está solicitando a requisição!";
    private static final String USUARIO_INFORMADO_NAO_ENCONTRADO = "Não foi possível encontrar o usuário que está sofrendo alterações!";
    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado!";
    private static final String SUPERADMIN_NAO_PODE_SE_DELETAR = "Superadmin não pode deletar a si mesmo!";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Transactional
    public UsuarioResponse incluir(IncluirUsuarioRequest request) {
        UUID idSolicitante = usuarioAutenticadoService.getId();
        Usuario solicitante = usuarioRepository.findById(idSolicitante)
                .orElseThrow(() -> new ResponseStatusException(UNPROCESSABLE_ENTITY, USUARIO_SOLICITANTE_NAO_ENCONTRADO));

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST, USUARIO_COM_EMAIL_EXISTENTE);
        }

        TipoUsuario tipoUsuarioCriador = solicitante.getTipo();
        TipoUsuario tipoUsuarioSendoCriado = request.getTipo();

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
                            ADMIN_CD_SO_PODE_CRIAR_E_DELETAR_VOLUNTARIOS);
                }
                break;

            default:
                // Admins abrigos e voluntários não criam ninguém
                throw new ResponseStatusException(FORBIDDEN, USUARIO_SEM_PERMISSAO_CRIACAO_DELECAO);
        }

        // Se passa aqui, pode criar sem problemas
        Usuario usuarioNovo = UsuarioMapper.toEntity(request);

//        String senhaAleatoria = PasswordGenerator.generateRandomPassword(10);

        // emailService.enviarSenhaAleatoria(usuarioNovo.getEmail(), "Criação de Conta -
        // SGDRS", senhaAleatoria);

//        System.out.println("Senha criada aleatoriamente: " + senhaAleatoria);

        usuarioNovo.setSenha(getSenhaCriptografada("1234"));
//        usuarioNovo.setSenha(getSenhaCriptografada(senhaAleatoria));

        Permissao permissao = getPermissao(request.getTipo());
        usuarioNovo.adicionarPermissao(permissao);
        usuarioNovo.setAtivo(true);

        if(tipoUsuarioCriador.equals(TipoUsuario.ADMIN_CD)){
            usuarioNovo.setCentroDistribuicao(solicitante.getCentroDistribuicao());
        }

        usuarioRepository.save(usuarioNovo);
        permissaoRepository.save(permissao);

        return UsuarioMapper.toResponse(usuarioNovo);
    }

    public List<UsuarioResponse> listarUsuarios(TipoUsuario tipoUsuario) {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getTipo().equals(tipoUsuario))
                .filter(Usuario::isAtivo)
                .map(UsuarioMapper::toResponse)
                .toList();
    }

    public List<UsuarioResponse> listarVoluntarios(UUID id_cd, String nome) {
        return usuarioRepository.findAll().stream()
            .filter(usuario -> usuario.getTipo().equals(TipoUsuario.VOLUNTARIO))
            .filter(usuario -> usuario.getCentroDistribuicao().getId().equals(id_cd))
            .filter(Usuario::isAtivo)
            .filter(usuario -> nome == null || nome.isEmpty() || usuario.getNome().toLowerCase().contains(nome.toLowerCase()))
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
                permissao.setFuncao(Funcao.ROLE_VOLUNTARIO);
                break;
            case ADMIN_CD:
                permissao.setFuncao(Funcao.ROLE_ADMIN_CD);
                break;
            case ADMIN_ABRIGO:
                permissao.setFuncao(Funcao.ROLE_ADMIN_ABRIGO);
                break;
            case SUPERADMIN:
                permissao.setFuncao(Funcao.ROLE_SUPERADMIN);
                break;
            default:
                throw new ResponseStatusException(BAD_REQUEST, "Tipo inválido");
        }
        return permissao;
    }

    public void excluir(UUID idUsuarioDeletado) {
        UUID idLogado = usuarioAutenticadoService.getId();
        Usuario usuarioSolicitante = usuarioRepository.findById(idLogado).orElseThrow(() -> new ResponseStatusException(UNPROCESSABLE_ENTITY, USUARIO_SOLICITANTE_NAO_ENCONTRADO));
        Optional<Usuario> optionalUsuarioDeletado = usuarioRepository.findById(idUsuarioDeletado);

        if (optionalUsuarioDeletado.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, USUARIO_INFORMADO_NAO_ENCONTRADO);
        }

        TipoUsuario tipoUsuarioSolicitante = usuarioSolicitante.getTipo();
        TipoUsuario tipoUsuarioDeletado = optionalUsuarioDeletado.get().getTipo();

        switch (tipoUsuarioSolicitante) {
            case SUPERADMIN: // Pode fazer tudo
                if(idLogado.equals(idUsuarioDeletado)){
                    throw new ResponseStatusException(BAD_REQUEST,
                            SUPERADMIN_NAO_PODE_SE_DELETAR);
                }
                break;

            case ADMIN_CD: // Só pode deletar voluntários
                if (!tipoUsuarioDeletado.equals(TipoUsuario.VOLUNTARIO)) {
                    throw new ResponseStatusException(FORBIDDEN,
                            ADMIN_CD_SO_PODE_CRIAR_E_DELETAR_VOLUNTARIOS);
                }
                break;

            default:
                // Admins abrigos e voluntários não deletam ninguém
                throw new ResponseStatusException(FORBIDDEN, USUARIO_SEM_PERMISSAO_CRIACAO_DELECAO);
        }

        // Desativar usuario
        Usuario usuarioDesativado = usuarioRepository.findById(idUsuarioDeletado).get();
        usuarioDesativado.setAtivo(false);
        usuarioRepository.save(usuarioDesativado);
    }

    public UsuarioResponse buscarInformacoesUsuario(UUID idUsuario) {
        Usuario buscado = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, USUARIO_NAO_ENCONTRADO));

        return UsuarioMapper.toResponse(buscado);
    }
}
