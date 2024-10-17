package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.response.IdResponse;

import java.util.UUID;

public class IdMapper {
    public static IdResponse toResponse(UUID id){
        return IdResponse.builder()
                .id(id)
                .build();
    }
}
