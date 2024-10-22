package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.domain.Item;

public class ItemMapper {

    public static ItemResponse toResponse(Item item){
        return ItemResponse.builder()
        .id(item.getId())
        .nome(item.getNome())
        .quantidade(item.getQuantidade())
        .descricao(item.getDescricao())
        .categoria(item.getCategoria())
        .build();
    }
    
}
