package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.IncluirPedidoRequest;
import br.com.sgdrs.controller.request.ItemRequest;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.domain.Abrigo;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Item;
import br.com.sgdrs.domain.Movimentacao;
import br.com.sgdrs.domain.Pedido;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.StatusPedido;
import br.com.sgdrs.mapper.IdMapper;
import br.com.sgdrs.mapper.MovimentacaoMapper;
import br.com.sgdrs.mapper.PedidoMapper;
import br.com.sgdrs.repository.CentroDistribuicaoRepository;
import br.com.sgdrs.repository.ItemRepository;
import br.com.sgdrs.repository.MovimentacaoRepository;
import br.com.sgdrs.repository.UsuarioRepository;
import br.com.sgdrs.repository.PedidoRepository;
import br.com.sgdrs.service.users.UsuarioAutenticadoService;
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
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;


@Service
public class PedidosService {
    private static final String USUARIO_NAO_VOLUNTARIO = "Acesso negado! Use credenciais de voluntário!";
    private static final String VOLUNTARIO_NAO_ENCONTRADO = "Voluntário não encontrado!";
    private static final String CENTRO_NAO_ENCONTRADO = "Centro de Distribuição não encontrado!";
    private static final String PEDIDO_NAO_ENCONTRADO = "Pedido não encontrado!";
    private static final String MENSAGEM_DESTINATARIO_INEXISTENTE = "O usuario receptor não existe";
    private static final String MENSAGEM_RECEPTOR_INVALIDO = "O usuario receptor não é um ADMINCD";
    private static final String MENSAGEM_PEDIDO_INEXISTENTE = "O Pedido não Existe";
    private static final String USUARIO_LOGADO_NAO_ENCONTRADO = "Usuário logado não encontrado";
    private static final String CENTRO_NAO_ACESSIVEL = "Admin e voluntário não são do mesmo centro de distribuição";
    

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private ItemRepository itemRepository;

    public List<PedidoResponse> listarPedidosVoluntarioById(UUID idVoluntario) {
        UUID idSolicitante = usuarioAutenticadoService.getId();
        Usuario solicitante = usuarioRepository.findById(idSolicitante)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, USUARIO_LOGADO_NAO_ENCONTRADO));

        Usuario voluntario = usuarioRepository.findById(idVoluntario)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, VOLUNTARIO_NAO_ENCONTRADO));


        if(!voluntario.getTipo().equals(VOLUNTARIO)){
            throw new ResponseStatusException(BAD_REQUEST, USUARIO_NAO_VOLUNTARIO);
        }

        if(!voluntario.getCentroDistribuicao().getId().equals(solicitante.getCentroDistribuicao().getId())){
            throw new ResponseStatusException(BAD_REQUEST, CENTRO_NAO_ACESSIVEL);
        }

        List<Pedido> pedidos = pedidoRepository.findByVoluntario(voluntario);

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();
    }

    public List<PedidoResponse> listarPedidosVoluntario(){
        UUID idVoluntario = usuarioAutenticadoService.getId();
        Usuario voluntario = usuarioRepository.findById(idVoluntario)
             .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, VOLUNTARIO_NAO_ENCONTRADO));

        if(!voluntario.getTipo().equals(VOLUNTARIO)){
                throw new ResponseStatusException(BAD_REQUEST, USUARIO_NAO_VOLUNTARIO);
        }

        List<Pedido> pedidos = pedidoRepository.findByVoluntario(voluntario);

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();    
    }

    public List<PedidoResponse> listaPedidosCentro() {
        UUID idSolicitante = usuarioAutenticadoService.getId();
        Usuario solicitante = usuarioRepository.findById(idSolicitante)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, USUARIO_LOGADO_NAO_ENCONTRADO));

        List<Pedido> pedidos = pedidoRepository.findByCentroDistribuicao(solicitante.getCentroDistribuicao());

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();
    }

    

    public PedidoResponse atribuirVoluntarioPedido(UUID idVoluntario, UUID idPedido) {
        UUID idSolicitante = usuarioAutenticadoService.getId();
        Usuario adminCd = usuarioRepository.findById(idSolicitante)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, USUARIO_LOGADO_NAO_ENCONTRADO));

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, PEDIDO_NAO_ENCONTRADO));
        Usuario voluntario = usuarioRepository.findById(idVoluntario)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, VOLUNTARIO_NAO_ENCONTRADO));

        if(!adminCd.getCentroDistribuicao().getId().equals(voluntario.getCentroDistribuicao().getId())){
            throw new ResponseStatusException(BAD_REQUEST, CENTRO_NAO_ACESSIVEL);
        }

        pedido.setVoluntario(voluntario);
        pedido.setStatus(EM_PREPARO);

        return PedidoMapper.toResponse(pedidoRepository.save(pedido));
    }


    @Transactional
    public IdResponse criarPedido(IncluirPedidoRequest request,UUID idDestinatario){
        UUID idSolicitante = usuarioAutenticadoService.getId();


        Abrigo abrigo = usuarioRepository.findById(idSolicitante).get().getAbrigo();
        CentroDistribuicao centroDistribuicao = centroDistribuicaoRepository.findById(idDestinatario)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, CENTRO_NAO_ENCONTRADO));

        Pedido pedido = new Pedido();
        pedido.setAbrigo(abrigo);
        pedido.setCentroDistribuicao(centroDistribuicao);
        pedido.setData(LocalDate.now());
        pedido.setStatus(StatusPedido.CRIADO);
        pedidoRepository.save(pedido);

        Movimentacao movimentacao = null;
        // Inserindo os campos id_item e quantidade para cada item na Tabela Movimentacao
        for(int i = 0;i < request.getItens().size(); i++){
            ItemRequest itemRequest = request.getItens().get(i);
            Optional<Item> item = itemRepository.findById(itemRequest.getId());
            if(item.isPresent()){
                movimentacao = MovimentacaoMapper.toEntity(itemRequest);
                movimentacao.setPedido(pedido);
                movimentacaoRepository.save(movimentacao);
            }else{
                throw new ResponseStatusException(NOT_FOUND, "O item" + item.get().getNome() + "está esgotado/não existe");
            }  
        }

       return IdMapper.toResponse(pedido.getId());
    }

    public PedidoResponse trocaStatus(String statusPedido,UUID id_pedido){
        Pedido pedido = pedidoRepository.findById(id_pedido)
                .orElseThrow(() -> new ResponseStatusException(UNPROCESSABLE_ENTITY, MENSAGEM_PEDIDO_INEXISTENTE));


        pedido.setStatus(StatusPedido.valueOf(statusPedido.toUpperCase()));
        pedidoRepository.save(pedido);

        return PedidoMapper.toResponse(pedido);

    }

   

    public List<PedidoResponse> listarPedidosAbrigo(StatusPedido status) {
        UUID idSolicitante = usuarioAutenticadoService.getId();
        Usuario solicitante = usuarioRepository.findById(idSolicitante)
                .orElseThrow(()->new ResponseStatusException(NOT_FOUND, USUARIO_LOGADO_NAO_ENCONTRADO));

        return pedidoRepository.findByAbrigo(solicitante.getAbrigo())
                .stream()
                .filter(pedido -> pedido.getStatus().equals(status) || isNull(status))
                .map(PedidoMapper::toResponse)
                .toList();
    }
}
