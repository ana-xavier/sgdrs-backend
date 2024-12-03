package br.com.sgdrs.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarItemRequest {
    @NotBlank(message = "Nome do item não pode ser vazio!")
    private String nome;

    @NotBlank(message = "Descrição do item não pode ser vazia!")
    private String descricao;

//    private int quantidade;

    @NotBlank(message = "Categoria do item não pode ser vazia!")
    private String categoria;

//    private String codBarras;

    @Min(value = 1, message = "O valor de medido não deve ser menor que 1!")
    private int valorMedida;

    @NotBlank(message = "A unidade de medida não pode ser vazia!")
    private String unidadeMedida;
}
