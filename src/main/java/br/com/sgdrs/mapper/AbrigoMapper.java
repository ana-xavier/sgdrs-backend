package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.request.IncluirAbrigoRequest;
import br.com.sgdrs.controller.response.AbrigoResponse;
import br.com.sgdrs.domain.Abrigo;

public class AbrigoMapper {
    public static Abrigo toEntity(IncluirAbrigoRequest request){
        return Abrigo.builder()
                .nome(request.getNome())
                .build();
    }

    public static AbrigoResponse toResponse(Abrigo abrigo){
        return AbrigoResponse.builder()
                .id(abrigo.getId())
                .nome(abrigo.getNome())
                .endereco(EnderecoMapper.toResponse(abrigo.getEndereco()))
                .build();
    }
}
