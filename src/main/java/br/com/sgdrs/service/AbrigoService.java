package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.EnderecoRequest;
import br.com.sgdrs.controller.request.IncluirAbrigoRequest;
import br.com.sgdrs.controller.request.IncluirPedidoRequest;
import br.com.sgdrs.controller.request.ItemRequest;
import br.com.sgdrs.controller.response.AbrigoResponse;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.domain.Endereco;
import br.com.sgdrs.domain.Item;
import br.com.sgdrs.domain.Movimentacao;
import br.com.sgdrs.domain.Pedido;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.StatusPedido;
import br.com.sgdrs.mapper.AbrigoMapper;
import br.com.sgdrs.mapper.EnderecoMapper;
import br.com.sgdrs.mapper.IdMapper;
import br.com.sgdrs.mapper.ItemMapper;
import br.com.sgdrs.mapper.MovimentacaoMapper;
import br.com.sgdrs.repository.AbrigoRepository;
import br.com.sgdrs.repository.CentroDistribuicaoRepository;
import br.com.sgdrs.repository.EnderecoRepository;
import br.com.sgdrs.domain.Abrigo;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.repository.IUsuarioRepository;
import br.com.sgdrs.repository.ItemRepository;
import br.com.sgdrs.repository.MovimentacaoRepository;
import br.com.sgdrs.repository.PedidoRepository;

import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.sgdrs.domain.enums.TipoUsuario.ADMIN_ABRIGO;
import static br.com.sgdrs.domain.enums.TipoUsuario.ADMIN_CD;
import static br.com.sgdrs.domain.enums.TipoUsuario.SUPERADMIN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
public class AbrigoService {
    private static final String MENSAGEM_ENDERECO_JA_EXISTENTE = "O endereço informado já é de outro abrigo";
    private static final String MENSAGEM_CRIADOR_INEXISTENTE = "O usuário criador não existe";
    private static final String MENSAGEM_CRIADORPEDIDO_INVALIDO = "O usuário criador não é um ADMINABRIGO";
    private static final String MENSAGEM_CRIADOR_INVALIDO = "O usuário criador não é um SUPERADMIN";
    private static final String MENSAGEM_DESTINATARIO_INEXISTENTE = "O usuario receptor não existe";
    private static final String MENSAGEM_RECEPTOR_INVALIDO = "O usuario receptor não é um ADMINCD";
    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired 
    private ItemRepository ItemRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    


    public List<AbrigoResponse> listar() {
        return abrigoRepository.findAll().stream()
                .map(AbrigoMapper::toResponse)
                .toList();
    }

    @Transactional
    public IdResponse criar(IncluirAbrigoRequest request, UUID idCriador) {
        Optional<Usuario> usuarioCriador = usuarioRepository.findById(idCriador);
        if(usuarioCriador.isEmpty()){
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, MENSAGEM_CRIADOR_INEXISTENTE);
        }

        if(!usuarioCriador.get().getTipo().equals(SUPERADMIN)){
            throw new ResponseStatusException(BAD_REQUEST, MENSAGEM_CRIADOR_INVALIDO);
        }

        EnderecoRequest enderecoRequest = request.getEndereco();
        Optional<Endereco> enderecoBuscado = enderecoRepository
                .findUniqueAddress(enderecoRequest.getCep(),
                    enderecoRequest.getLogradouro(),
                    enderecoRequest.getNumero(),
                    enderecoRequest.getCidade(),
                    enderecoRequest.getEstado());

        if(enderecoBuscado.isPresent()){
            throw new ResponseStatusException(BAD_REQUEST, MENSAGEM_ENDERECO_JA_EXISTENTE);
        }

        Endereco enderecoAbrigo = EnderecoMapper.toEntity(request.getEndereco());
        enderecoRepository.save(enderecoAbrigo);

        Abrigo abrigo = AbrigoMapper.toEntity(request);
        abrigo.setEndereco(enderecoAbrigo);

        abrigoRepository.save(abrigo);

        return IdMapper.toResponse(abrigo.getId());
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
    
    // Lista todos os itens
    public List<ItemResponse> listarItens() {
        return ItemRepository.findAll().stream()
                .map(ItemMapper::toResponse)
                .toList();
    }
}
