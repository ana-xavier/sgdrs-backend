package br.com.sgdrs.service.users;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.enums.Funcao;
import br.com.sgdrs.domain.Permissao;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.mapper.UsuarioMapper;
import br.com.sgdrs.repository.IUsuarioRepository;
import br.com.sgdrs.service.util.EmailService;
import br.com.sgdrs.service.util.PasswordGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.sgdrs.domain.enums.TipoUsuario.SUPERADMIN;
import static org.springframework.http.HttpStatus.*;

@Service
public class UsuarioService {

    public static final String SUPERADMIN_NAO_PODE_CRIAR_VOLUNTARIOS = "Este usuário não tem permissão para criar voluntários!";
    public static final String ADMIN_CD_SO_PODE_CRIAR_E_DELETAR_VOLUNTARIOS = "Este usuário só pode criar e deletar voluntários!";
    public static final String USUARIO_SEM_PERMISSAO_CRIACAO_DELECAO = "Este usuário não tem permissão para criar ou deletar outros usuários!";
    public static final String USUARIO_COM_EMAIL_EXISTENTE = "O e-mail informado já possui uma conta criada!";
    public static final String USUARIO_SOLICITANTE_NAO_ENCONTRADO = "Não foi possível encontrar o usuário que está solicitando a requisição!";
    public static final String USUARIO_INFORMADO_NAO_ENCONTRADO = "Não foi possível encontrar o usuário que está sofrendo alterações!";
    public static final String SUPERADMIN_NAO_ENCONTRADO = "Erro interno. Código 001";
    private static final String ACESSO_NAO_AUTORIZADO = "Acesso não autorizado!";
    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado!";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public UsuarioResponse incluir(IncluirUsuarioRequest request, UUID id_criador) {

        Optional<Usuario> optionalUsuarioCriador = usuarioRepository.findById(id_criador);
        TipoUsuario tipoUsuarioCriador;
        TipoUsuario tipoUsuarioSendoCriado;

        // Verificar que criador existe e criado não
        if (optionalUsuarioCriador.isEmpty()) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, USUARIO_SOLICITANTE_NAO_ENCONTRADO);
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
                            ADMIN_CD_SO_PODE_CRIAR_E_DELETAR_VOLUNTARIOS);
                }
                break;

            default:
                // Admins abrigos e voluntários não criam ninguém
                throw new ResponseStatusException(FORBIDDEN, USUARIO_SEM_PERMISSAO_CRIACAO_DELECAO);
        }

        // Se passa aqui, pode criar sem problemas
        Usuario usuarioNovo = UsuarioMapper.toEntity(request);

        String senhaAleatoria = PasswordGenerator.generateRandomPassword(10);

        // emailService.enviarSenhaAleatoria(usuarioNovo.getEmail(), "Criação de Conta -
        // SGDRS", senhaAleatoria);

        System.out.println("Senha criada aleatoriamente: " + senhaAleatoria);

        usuarioNovo.setSenha(getSenhaCriptografada(senhaAleatoria));
        usuarioNovo.adicionarPermissao(getPermissao(request.getTipo()));
        usuarioNovo.setAtivo(true);

        usuarioRepository.save(usuarioNovo);

        return UsuarioMapper.toResponse(usuarioNovo);
    }

    public List<UsuarioResponse> listarUsuarios(TipoUsuario tipoUsuario) {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getTipo().equals(tipoUsuario))
                .filter(usuario -> usuario.isAtivo() == true)
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

    public void excluir(UUID idSolicitante, UUID idUsuarioDeletado) {
        Optional<Usuario> optionalUsuarioSolicitante = usuarioRepository.findById(idSolicitante);
        Optional<Usuario> optionalUsuarioDeletado = usuarioRepository.findById(idUsuarioDeletado);

        // Verificar que criador existe e criado não
        if (optionalUsuarioSolicitante.isEmpty()) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, USUARIO_SOLICITANTE_NAO_ENCONTRADO);
        } else if (optionalUsuarioDeletado.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, USUARIO_INFORMADO_NAO_ENCONTRADO);
        }

        TipoUsuario tipoUsuarioSolicitante = optionalUsuarioSolicitante.get().getTipo();
        TipoUsuario tipoUsuarioDeletado = optionalUsuarioDeletado.get().getTipo();

        switch (tipoUsuarioSolicitante) {
            case SUPERADMIN: // Pode fazer tudo
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

    public UsuarioResponse buscarInformacoesUsuario(UUID idUsuarioSolicitante, UUID idUsuario) {
        Optional<Usuario> solicitante = usuarioRepository.findById(idUsuarioSolicitante);

        if(solicitante.isEmpty()){
            throw new ResponseStatusException(NOT_FOUND, SUPERADMIN_NAO_ENCONTRADO);
        }
        
        Usuario solicitanteObj = solicitante.get();
        
        if(!solicitanteObj.getTipo().equals(SUPERADMIN)){
            throw new ResponseStatusException(UNAUTHORIZED, ACESSO_NAO_AUTORIZADO);
        }

        Optional<Usuario> buscado = usuarioRepository.findById(idUsuario);

        if(buscado.isEmpty()){
            throw new ResponseStatusException(NOT_FOUND, USUARIO_NAO_ENCONTRADO);
        }

        Usuario buscadoObj = buscado.get();

        return UsuarioMapper.toResponse(buscadoObj);
    }
}
