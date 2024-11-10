package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.controller.response.ItemVerificadoResponse;
import br.com.sgdrs.domain.Item;

public class ItemVerificadoMapper {

    public static ItemVerificadoResponse toResponse(Item item,boolean status){
        ItemResponse Item = ItemMapper.toResponse(item);
        
       return ItemVerificadoResponse.builder()
       .item(Item)
       .status(status)
       .build();   
    }
    
}
