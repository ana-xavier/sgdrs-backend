package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.domain.Pedido;

import java.util.stream.Collectors;

public class PedidoMapper {
    public static PedidoResponse toResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .data(pedido.getData())
                .status(pedido.getStatus())
                .abrigo(pedido.getAbrigo() != null ? AbrigoMapper.toResponse(pedido.getAbrigo()) : null)
                .centroDistribuicao(pedido.getCentroDistribuicao() != null ? CentroDistribuicaoMapper.toResponse(pedido.getCentroDistribuicao()) : null)
                .voluntario(pedido.getVoluntario() != null ? UsuarioMapper.toResponse(pedido.getVoluntario()) : null)
                .itens(pedido.getItensPedido().stream()
                        .map(MovimentacaoMapper::toResponse)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
