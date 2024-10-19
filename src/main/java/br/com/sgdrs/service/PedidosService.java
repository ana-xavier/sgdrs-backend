package br.com.sgdrs.service;

import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Pedido;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.mapper.PedidoMapper;
import br.com.sgdrs.repository.CentroDistribuicaoRepository;
import br.com.sgdrs.repository.IUsuarioRepository;
import br.com.sgdrs.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.sgdrs.domain.enums.TipoUsuario.VOLUNTARIO;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class PedidosService {
    private static final String USUARIO_NAO_VOLUNTARIO = "Acesso negado! Use credenciais de voluntário!";
    private static final String VOLUNTARIO_NAO_ENCONTADO = "Voluntário não encontrado!";
    private static final String CENTRO_NAO_ENCONTADO = "Centro de Distribuição não encontrado!";

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<PedidoResponse> listarPedidosVoluntario(UUID idVoluntario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idVoluntario);

        if(usuarioOpt.isEmpty()){
            throw new ResponseStatusException(NOT_FOUND, VOLUNTARIO_NAO_ENCONTADO);
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
            throw new ResponseStatusException(NOT_FOUND, CENTRO_NAO_ENCONTADO);
        }

        CentroDistribuicao centro = centroOpt.get();

        List<Pedido> pedidos = pedidoRepository.findByCentroDistribuicao(centro);

        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();
    }
}
