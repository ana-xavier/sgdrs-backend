package br.com.sgdrs.controller.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoMovimentacaoResponse {
    private UUID id;
    private ItemResponse item;
    private int quantidade;
}
