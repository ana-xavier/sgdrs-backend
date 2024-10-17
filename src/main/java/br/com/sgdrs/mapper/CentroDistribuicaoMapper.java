package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.request.CentroDistribuicaoRequest;
import br.com.sgdrs.controller.response.CentroDistribuicaoResponse;
import br.com.sgdrs.domain.CentroDistribuicao;


public class CentroDistribuicaoMapper {

    public static CentroDistribuicao toEntity(CentroDistribuicaoRequest centroDistribuicaoRequest){
        return CentroDistribuicao.builder()
                .nome(centroDistribuicaoRequest.getNome())
                .endereco(EnderecoMapper.toEntity(centroDistribuicaoRequest.getEndereco()))
                .build();
    }

    public static CentroDistribuicaoResponse toResponse(CentroDistribuicao centro){
        return CentroDistribuicaoResponse.builder()
                .id(centro.getId())
                .nome(centro.getNome())
                .endereco(EnderecoMapper.toResponse(centro.getEndereco()))
                .build();
    }
}
