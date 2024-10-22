package br.com.sgdrs.controller.response;

import br.com.sgdrs.domain.enums.StatusPedido;
import lombok.*;


import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private UUID id;
    private LocalDate data;
    private UUID idAbrigo;
    private UUID idCD;
    private UUID idVoluntario;
    private StatusPedido status;
}
