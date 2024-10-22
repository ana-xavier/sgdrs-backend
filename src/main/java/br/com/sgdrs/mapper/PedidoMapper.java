package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.domain.Pedido;

public class PedidoMapper {
    public static PedidoResponse toResponse(Pedido pedido){
        return PedidoResponse.builder()
                .id(pedido.getId())
                .data(pedido.getData())
                .status(pedido.getStatus())
                .idAbrigo(pedido.getAbrigo().getId())
                .idCD(pedido.getCentroDistribuicao().getId())
                .idVoluntario(pedido.getVoluntario().getId())
                .build();
    }
}
