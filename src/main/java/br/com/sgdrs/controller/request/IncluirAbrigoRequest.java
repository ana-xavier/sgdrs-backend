package br.com.sgdrs.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncluirAbrigoRequest {
    @NotBlank(message = "Nome do abrigo é obrigatório")
    @Size(max = 250, message = "Nome do abrigo deve ter até 250 caracteres")
    private String nome;

    @NotNull(message = "Endereço é obrigatório")
    private EnderecoRequest endereco;

}
