package br.com.sgdrs.controller.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CentroDistribuicaoResponse {
    private UUID id;
    private String nome;
    private EnderecoResponse endereco;
}
