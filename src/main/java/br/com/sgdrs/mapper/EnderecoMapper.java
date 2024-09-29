package br.com.sgdrs.mapper;

import br.com.sgdrs.controller.request.EnderecoRequest;
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
}
