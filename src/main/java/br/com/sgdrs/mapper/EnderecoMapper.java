package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.request.EnderecoRequest;
import br.com.sgdrs.controller.response.EnderecoResponse;
import br.com.sgdrs.domain.Endereco;

public class EnderecoMapper {

    public static Endereco toEntity(EnderecoRequest request) {
        return Endereco.builder()
                .cep(request.getCep())
                .logradouro(request.getLogradouro())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .numero(request.getNumero())
                .build();
    }

    public static EnderecoResponse toResponse(Endereco endereco){
        return EnderecoResponse.builder()
                .cep(endereco.getCep())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .logradouro(endereco.getLogradouro())
                .bairro(endereco.getBairro())
                .numero(endereco.getNumero())
                .build();
    }
}
