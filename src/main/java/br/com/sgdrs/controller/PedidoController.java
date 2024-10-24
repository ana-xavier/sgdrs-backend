package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.IncluirPedidoRequest;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.domain.enums.StatusPedido;
import br.com.sgdrs.service.PedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    
    @Autowired
    private PedidosService pedidosService;

    @GetMapping("/voluntario/{idVoluntario}")
    @ResponseStatus(OK)
    public List<PedidoResponse> listarPedidosVoluntario(@PathVariable UUID idVoluntario){
        return pedidosService.listarPedidosVoluntario(idVoluntario);
    }

    @GetMapping("/centro/{idCentro}")
    @ResponseStatus(OK)
    public List<PedidoResponse> listarPedidosCentro(@PathVariable UUID idCentro){
        return pedidosService.listaPedidosCentro(idCentro);
    }

    @PatchMapping("/atribuir-voluntario/admin/{id_admin}/voluntario/{id_voluntario}/pedido/{id_pedido}")
    public ResponseEntity<PedidoResponse> atribuirVoluntarioPedido(@PathVariable(value = "id_voluntario") UUID idVoluntario,
                                                                   @PathVariable(value = "id_pedido") UUID idPedido,
                                                                   @PathVariable(value = "id_admin") UUID idAdmin) {
        return new ResponseEntity<>(pedidosService.atribuirVoluntarioPedido(idVoluntario, idPedido, idAdmin), OK);
    }

    @PostMapping("/criar-pedido/{idCriador}/{idDestinatario}")
    @ResponseStatus(CREATED)
    public IdResponse criarPedido(@RequestBody IncluirPedidoRequest request, @PathVariable UUID idCriador,@PathVariable UUID idDestinatario){
        return pedidosService.criarPedido(request, idCriador,idDestinatario);
    }


    @PostMapping("/troca-status/{id_pedido}/{status_pedido}")
    @ResponseStatus(OK)
    public PedidoResponse trocaStatus(@PathVariable UUID id_pedido, @PathVariable String status_pedido){
        return pedidosService.trocaStatus(status_pedido,id_pedido);
    }

}
