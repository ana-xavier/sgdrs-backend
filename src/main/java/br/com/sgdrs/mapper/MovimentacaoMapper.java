package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.request.ItemRequest;
import br.com.sgdrs.controller.response.PedidoMovimentacaoResponse;
import br.com.sgdrs.domain.Item;
import br.com.sgdrs.domain.Movimentacao;


public class MovimentacaoMapper {
    public static Movimentacao toEntity(ItemRequest request) {
        Item item = Item.builder()
                        .id(request.getId())  
                        .build();
        return Movimentacao.builder()
                .item(item)  
                .quantidade(request.getQuantidade())
                .build();
    }

    public static PedidoMovimentacaoResponse toResponse(Movimentacao movimentacao){
        return PedidoMovimentacaoResponse.builder()
                .id(movimentacao.getId())
                .item(ItemMapper.toResponse(movimentacao.getItem()))
                .quantidade(movimentacao.getQuantidade())
                .build();
    }

}
