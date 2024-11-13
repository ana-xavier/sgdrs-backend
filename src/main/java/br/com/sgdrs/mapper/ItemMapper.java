package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.domain.Item;

public class ItemMapper {

    public static Item toEntity(Item item){
        return Item.builder()
        .id(item.getId())
        .categoria(item.getCategoria())
        .nome(item.getNome())
        .quantidade(0)
        .centroDistribuicao(item.getCentroDistribuicao())
        .descricao(item.getDescricao())
        .codBarras(item.getCodBarras())
        .unidade_medida(item.getUnidade_medida())
        .build();

    }

    public static ItemResponse toResponse(Item item){
        return ItemResponse.builder()
        .id(item.getId())
        .nome(item.getNome())
        .quantidade(item.getQuantidade())
        .descricao(item.getDescricao())
        .categoria(item.getCategoria())
        .codBarras(item.getCodBarras())
        .unidade_medida(item.getUnidade_medida())
        .validado(item.isValidado())
        .build();
    }

    
    
}
