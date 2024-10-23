package br.com.sgdrs.service;

import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Pedido;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.mapper.PedidoMapper;
import br.com.sgdrs.repository.CentroDistribuicaoRepository;
import br.com.sgdrs.repository.UsuarioRepository;
import br.com.sgdrs.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.sgdrs.domain.enums.StatusPedido.EM_PREPARO;
import static br.com.sgdrs.domain.enums.TipoUsuario.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class PedidosService {
    private static final String USUARIO_NAO_VOLUNTARIO = "Acesso negado! Use credenciais de voluntário!";
    private static final String USUARIO_NAO_ADMIN = "Acesso negado! Use credenciais de admin!";
    private static final String VOLUNTARIO_NAO_ENCONTRADO = "Voluntário não encontrado!";
    private static final String CENTRO_NAO_ENCONTRADO = "Centro de Distribuição não encontrado!";
    private static final String PEDIDO_NAO_ENCONTRADO = "Pedido não encontrado!";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

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
        pedidoOptional.get().setStatus(EM_PREPARO);

        return PedidoMapper.toResponse(pedidoRepository.save(pedidoOptional.get()));
    }

    private boolean validarUsuarioAdmin(Usuario usuario) {
        return (usuario.getTipo().equals(ADMIN_CD)) || (usuario.getTipo().equals(ADMIN_ABRIGO));
    }
}
