package br.com.sgdrs.factories;

import br.com.sgdrs.domain.Abrigo;

import static java.util.UUID.randomUUID;

public class AbrigoFactory {
    public static Abrigo.AbrigoBuilder get(){
        return Abrigo.builder()
                .id(randomUUID())
                .nome("nome abrigo")
                .endereco(EnderecoFactory.getEndereco());
    }

    public static Abrigo getAbrigo(){
        return get().build();
    }
}
