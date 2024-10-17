package br.com.sgdrs.factories;

import br.com.sgdrs.controller.request.IncluirAbrigoRequest;

public class IncluirAbrigoRequestFactory {
    public static IncluirAbrigoRequest getRequest(){
        return IncluirAbrigoRequest.builder()
                .nome("abrigo")
                .endereco(EnderecoRequestFactory.getRequest())
                .build();
    }
}
