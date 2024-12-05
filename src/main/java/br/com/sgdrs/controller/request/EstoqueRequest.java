package br.com.sgdrs.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueRequest {
    @NotNull(message = "Os itens são obrigatórios")
    private List<EstoqueItem> itens;

    @NotNull(message = "O id do Doador é obrigatório")
    private UUID id_doador;

    
}

   




