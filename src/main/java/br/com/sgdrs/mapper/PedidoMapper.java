package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.domain.Pedido;

public class PedidoMapper {
    public static PedidoResponse toResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .data(pedido.getData())
                .status(pedido.getStatus())
                .idAbrigo(pedido.getAbrigo() != null ? pedido.getAbrigo().getId() : null)
                .idCD(pedido.getCentroDistribuicao() != null ? pedido.getCentroDistribuicao().getId() : null)
                .idVoluntario(pedido.getVoluntario() != null ? pedido.getVoluntario().getId() : null)
                .build();
    }
}
