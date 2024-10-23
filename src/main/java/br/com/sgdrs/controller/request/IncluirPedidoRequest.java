package br.com.sgdrs.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncluirPedidoRequest {
    @NotNull(message = "Os itens são obrigatórios")
    private List<ItemRequest> itens; 
}


