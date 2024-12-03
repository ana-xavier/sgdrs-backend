package br.com.sgdrs.controller.response;

import br.com.sgdrs.domain.enums.StatusPedido;
import lombok.*;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private UUID id;
    private LocalDate data;
    private AbrigoResponse abrigo;
    private CentroDistribuicaoResponse centroDistribuicao;
    private UsuarioResponse voluntario;
    private StatusPedido status;
    private List<PedidoMovimentacaoResponse> itens;
}
