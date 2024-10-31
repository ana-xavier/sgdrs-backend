package br.com.sgdrs.factories;

import br.com.sgdrs.controller.request.CentroDistribuicaoRequest;

public class CentroDistribuicaoRequestFactory {
    private static CentroDistribuicaoRequest.CentroDistribuicaoRequestBuilder get(){
        return CentroDistribuicaoRequest.builder()
                .nome("nome")
                .endereco(EnderecoRequestFactory.getRequest());
    }

    public static CentroDistribuicaoRequest getRequest(){
        return get().build();
    }
}
