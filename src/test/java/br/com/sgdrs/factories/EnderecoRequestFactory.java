package br.com.sgdrs.factories;

import br.com.sgdrs.controller.request.EnderecoRequest;

public class EnderecoRequestFactory {
    public static EnderecoRequest getRequest(){
        return EnderecoRequest.builder()
                .logradouro("rua")
                .bairro("bairro")
                .estado("rs")
                .cidade("cidade")
                .cep("123456789")
                .numero("123")
                .build();
    }
}
