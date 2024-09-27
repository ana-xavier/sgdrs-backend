package br.com.sgdrs.controller.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbrigoResponse {
    private UUID id;
    private String nome;
    private EnderecoResponse endereco;
}
