package br.com.sgdrs.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CentroDistribuicaoRequest {
    @NotBlank(message = "Nome do abrigo é obrigatório")
    @Size(max = 250, message = "Nome do centro de distribuição deve ter até 250 caracteres")
    private String nome;

    @NotNull(message = "Endereço é obrigatório")
    private EnderecoRequest endereco;
}
