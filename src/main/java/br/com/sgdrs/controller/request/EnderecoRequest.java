package br.com.sgdrs.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoRequest {
    @NotBlank(message = "CEP é obrigatório")
    @Size(max = 10, message = "CEP deve ter até 10 caracteres")
    private String cep;

    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 250, message = "Logradouro deve ter até 250 caracteres")
    private String logradouro;

    @Size(max = 250, message = "Numero deve ter até 250 caracteres")
    private String numero;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 250, message = "Bairro deve ter até 250 caracteres")
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 250, message = "Cidade deve ter até 250 caracteres")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
    private String estado;
}
