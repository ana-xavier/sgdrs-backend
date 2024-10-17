package br.com.sgdrs.factories;

import br.com.sgdrs.domain.Endereco;

import static java.util.UUID.randomUUID;

public class EnderecoFactory {
    public static Endereco.EnderecoBuilder get(){
        return Endereco.builder()
                .id(randomUUID())
                .cidade("cidade")
                .cep("123456789")
                .logradouro("rua")
                .estado("rs")
                .bairro("bairro")
                .numero("123");
    }

    public static Endereco getEndereco(){
        return get().build();
    }
}
