package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.IncluirPedidoRequest;
import br.com.sgdrs.controller.request.ItemRequest;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.domain.Abrigo;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Movimentacao;
import br.com.sgdrs.domain.Pedido;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.StatusPedido;
import br.com.sgdrs.mapper.IdMapper;
import br.com.sgdrs.mapper.MovimentacaoMapper;
import br.com.sgdrs.mapper.PedidoMapper;
import br.com.sgdrs.repository.CentroDistribuicaoRepository;
import br.com.sgdrs.repository.IUsuarioRepository;
import br.com.sgdrs.repository.MovimentacaoRepository;
import br.com.sgdrs.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.sgdrs.domain.enums.StatusPedido.EM_PREPARO;
import static br.com.sgdrs.domain.enums.TipoUsuario.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;


@Service
public class PedidosService {
    private static final String USUARIO_NAO_VOLUNTARIO = "Acesso negado! Use credenciais de voluntário!";
    private static final String USUARIO_NAO_ADMIN = "Acesso negado! Use credenciais de admin!";
    private static final String VOLUNTARIO_NAO_ENCONTRADO = "Voluntário não encontrado!";
    private static final String CENTRO_NAO_ENCONTRADO = "Centro de Distribuição não encontrado!";
    private static final String PEDIDO_NAO_ENCONTRADO = "Pedido não encontrado!";
    private static final String MENSAGEM_CRIADOR_INEXISTENTE = "O usuário criador não existe";
    private static final String MENSAGEM_CRIADORPEDIDO_INVALIDO = "O usuário criador não é um ADMINABRIGO";
    private static final String MENSAGEM_DESTINATARIO_INEXISTENTE = "O usuario receptor não existe";
    private static final String MENSAGEM_RECEPTOR_INVALIDO = "O usuario receptor não é um ADMINCD";
    private static final String MENSAGEM_PEDIDO_INEXISTENTE = "O Pedido não Existe";

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    public List<PedidoResponse> listarPedidosVoluntario(UUID idVoluntario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idVoluntario);

        if(usuarioOpt.isEmpty()){
            throw new ResponseStatusException(NOT_FOUND, VOLUNTARIO_NAO_ENCONTRADO);
        }

        Usuario voluntario = usuarioOpt.get();

        if(!voluntario.getTipo().equals(VOLUNTARIO)){
            throw new ResponseStatusException(BAD_REQUEST, USUARIO_NAO_VOLUNTARIO);
        }

        List<Pedido> pedidos = pedidoRepository.findByVoluntario(voluntario);

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();
    }

    public List<PedidoResponse> listaPedidosCentro(UUID idCentro) {
        Optional<CentroDistribuicao> centroOpt = centroDistribuicaoRepository.findById(idCentro);

        if(centroOpt.isEmpty()){
            throw new ResponseStatusException(NOT_FOUND, CENTRO_NAO_ENCONTRADO);
        }

        CentroDistribuicao centro = centroOpt.get();

        List<Pedido> pedidos = pedidoRepository.findByCentroDistribuicao(centro);

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();
    }

    public PedidoResponse atribuirVoluntarioPedido(UUID idVoluntario, UUID idPedido, UUID idAdmin) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(idPedido);
        Optional<Usuario> voluntarioOptional = usuarioRepository.findById(idVoluntario);
        Optional<Usuario> adminOptional = usuarioRepository.findById(idAdmin);

        if (!validarUsuarioAdmin(adminOptional.get())) {
            throw new ResponseStatusException(BAD_REQUEST, USUARIO_NAO_ADMIN);
        }
        if (pedidoOptional.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, PEDIDO_NAO_ENCONTRADO);
        }
        if (voluntarioOptional.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, VOLUNTARIO_NAO_ENCONTRADO);
        }

        pedidoOptional.get().setVoluntario(voluntarioOptional.get());

        return PedidoMapper.toResponse(pedidoRepository.save(pedidoOptional.get()));
    }

    private boolean validarUsuarioAdmin(Usuario usuario) {
        return (usuario.getTipo().equals(ADMIN_CD)) || (usuario.getTipo().equals(ADMIN_ABRIGO));
    }



    @Transactional
    public IdResponse criarPedido(IncluirPedidoRequest request,UUID idCriador,UUID idDestinatario){
        Movimentacao movimentacao = new Movimentacao();
        Pedido pedido = new Pedido();

        Optional<Usuario> usuarioCriador = usuarioRepository.findById(idCriador);
        if(usuarioCriador.isEmpty()){
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, MENSAGEM_CRIADOR_INEXISTENTE);
        }

        Optional<Usuario> usuarioReceptor = usuarioRepository.findById(idDestinatario);
        if(usuarioReceptor.isEmpty()){
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, MENSAGEM_DESTINATARIO_INEXISTENTE);
        }

        if(!usuarioCriador.get().getTipo().equals(ADMIN_ABRIGO)){
            throw new ResponseStatusException(BAD_REQUEST, MENSAGEM_CRIADORPEDIDO_INVALIDO);
        }

        if(!usuarioReceptor.get().getTipo().equals(ADMIN_CD)){
            throw new ResponseStatusException(BAD_REQUEST, MENSAGEM_RECEPTOR_INVALIDO);
        }


        Abrigo abrigo = usuarioRepository.findById(idCriador).get().getAbrigo();
        CentroDistribuicao centroDistribuicao = usuarioRepository.findById(idDestinatario).get().getCentroDistribuicao();
        

        pedido.setAbrigo(abrigo);
        pedido.setCentroDistribuicao(centroDistribuicao);
        pedido.setData(LocalDate.now());
        pedido.setStatus(StatusPedido.CRIADO);
        pedidoRepository.save(pedido);
        

        // Inserindo os campos id_item e quantidade para cada item na Tabela Movimentacao
        for(int i = 0;i < request.getItens().size(); i++){
            ItemRequest itemRequest = request.getItens().get(i);
            movimentacao = MovimentacaoMapper.toEntity(itemRequest);
            movimentacao.setPedido(pedido);
            movimentacaoRepository.save(movimentacao);            
        }

       return IdMapper.toResponse(pedido.getId());
    }

    public PedidoResponse trocaStatus(String statusPedido,UUID id_pedido){
        Optional<Pedido> pedido = pedidoRepository.findById(id_pedido);
        
        if(pedido.isEmpty()){
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, MENSAGEM_PEDIDO_INEXISTENTE);
        }

        Pedido pedidoAtual = pedido.get();
        pedidoAtual.setStatus(StatusPedido.valueOf(statusPedido.toUpperCase()));
        pedidoRepository.save(pedidoAtual);

        return PedidoMapper.toResponse(pedidoAtual);

    }
    
}
