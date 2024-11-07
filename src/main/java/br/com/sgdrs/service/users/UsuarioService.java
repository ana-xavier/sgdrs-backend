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
import br.com.sgdrs.service.util.BuscarUsuarioLogadoService;
import br.com.sgdrs.service.util.EmailService;
import br.com.sgdrs.service.util.PasswordGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static br.com.sgdrs.domain.enums.TipoUsuario.*;
import static org.springframework.http.HttpStatus.*;

@Service
public class UsuarioService {

    private static final String SUPERADMIN_NAO_PODE_CRIAR_VOLUNTARIOS = "Este usuário não tem permissão para criar voluntários!";
    private static final String ADMIN_CD_SO_PODE_CRIAR_E_DELETAR_VOLUNTARIOS = "Este usuário só pode criar e deletar voluntários!";
    private static final String USUARIO_SEM_PERMISSAO_CRIACAO_DELECAO = "Este usuário não tem permissão para criar ou deletar outros usuários!";
    private static final String USUARIO_COM_EMAIL_EXISTENTE = "O e-mail informado já possui uma conta criada!";
    private static final String USUARIO_INFORMADO_NAO_ENCONTRADO = "Não foi possível encontrar o usuário que está sofrendo alterações!";
    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado!";
    private static final String SUPERADMIN_NAO_PODE_SE_DELETAR = "Superadmin não pode deletar a si mesmo!";
    private static final String ADMIN_CD_NAO_PODE_REATIVAR = "Admin_CD não pode reativar usuários não voluntários!";
    private static final String CENTRO_NAO_ACESSIVEL = "Admin e voluntário não são do mesmo centro de distribuição";

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

    @Autowired
    private BuscarUsuarioLogadoService buscarUsuarioLogadoService;

    @Transactional
    public UsuarioResponse incluir(IncluirUsuarioRequest request) {
        Usuario solicitante = buscarUsuarioLogadoService.getLogado();


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

        String senhaAleatoria = PasswordGenerator.generateRandomPassword(10);

        // emailService.enviarSenhaAleatoria(usuarioNovo.getEmail(), "Criação de Conta -
        // SGDRS", senhaAleatoria);

        System.out.println("Senha criada aleatoriamente: " + senhaAleatoria);

        usuarioNovo.setSenha(getSenhaCriptografada(senhaAleatoria));

        Permissao permissao = getPermissao(request.getTipo());
        usuarioNovo.adicionarPermissao(permissao);
        usuarioNovo.setAtivo(true);

        if (tipoUsuarioCriador.equals(TipoUsuario.ADMIN_CD)) {
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

    public List<UsuarioResponse> listarVoluntarios(String nome) {
        Usuario solicitante = buscarUsuarioLogadoService.getLogado();

        return usuarioRepository.findAll().stream()
            .filter(usuario -> usuario.getTipo().equals(TipoUsuario.VOLUNTARIO))
            .filter(usuario -> usuario.getCentroDistribuicao().getId().equals(solicitante.getCentroDistribuicao().getId()))
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
        Usuario solicitante = buscarUsuarioLogadoService.getLogado();

        Usuario usuarioDeletado = usuarioRepository.findById(idUsuarioDeletado)
                .orElseThrow(()->new ResponseStatusException(BAD_REQUEST, USUARIO_INFORMADO_NAO_ENCONTRADO));

        TipoUsuario tipoUsuarioSolicitante = solicitante.getTipo();
        TipoUsuario tipoUsuarioDeletado = usuarioDeletado.getTipo();

        switch (tipoUsuarioSolicitante) {
            case SUPERADMIN: // Pode fazer tudo
                if(solicitante.getId().equals(idUsuarioDeletado)){
                    throw new ResponseStatusException(BAD_REQUEST,
                            SUPERADMIN_NAO_PODE_SE_DELETAR);
                }
                break;

            case ADMIN_CD: // Só pode deletar voluntários
                if (!tipoUsuarioDeletado.equals(TipoUsuario.VOLUNTARIO)) {
                    throw new ResponseStatusException(FORBIDDEN,
                            ADMIN_CD_SO_PODE_CRIAR_E_DELETAR_VOLUNTARIOS);
                }

                if(!solicitante.getCentroDistribuicao().equals(usuarioDeletado.getCentroDistribuicao())){
                    throw new ResponseStatusException(BAD_REQUEST, CENTRO_NAO_ACESSIVEL);
                }
                break;

            default:
                // Admins abrigos e voluntários não deletam ninguém
                throw new ResponseStatusException(FORBIDDEN, USUARIO_SEM_PERMISSAO_CRIACAO_DELECAO);
        }



        // Desativar usuario
        usuarioDeletado.setAtivo(false);
        usuarioRepository.save(usuarioDeletado);
    }

    public UsuarioResponse buscarInformacoesUsuario(UUID idUsuario) {
        Usuario buscado = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, USUARIO_NAO_ENCONTRADO));

        return UsuarioMapper.toResponse(buscado);
    }

    public void reativar(UUID idUsuario) {
        Usuario solicitante = buscarUsuarioLogadoService.getLogado();

        Usuario reativado = usuarioRepository.findById(idUsuario)
                .orElseThrow(()->new ResponseStatusException(NOT_FOUND, USUARIO_NAO_ENCONTRADO));

        if(solicitante.getTipo().equals(ADMIN_CD)){
            if(!reativado.getTipo().equals(VOLUNTARIO)){
                throw new ResponseStatusException(FORBIDDEN, ADMIN_CD_NAO_PODE_REATIVAR);
            }
            if(!solicitante.getCentroDistribuicao().equals(reativado.getCentroDistribuicao())){
                throw new ResponseStatusException(UNPROCESSABLE_ENTITY, CENTRO_NAO_ACESSIVEL);
            }
        }

        reativado.setAtivo(true);
        usuarioRepository.save(reativado);
    }
}
