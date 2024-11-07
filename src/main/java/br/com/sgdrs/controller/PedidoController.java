package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.IncluirPedidoRequest;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.domain.enums.StatusPedido;
import br.com.sgdrs.service.PedidosService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RolesAllowed({"ADMIN_CD"})
    @GetMapping("/voluntario/{idVoluntario}")
    @ResponseStatus(OK)
    public List<PedidoResponse> listarPedidosVoluntarioById(@PathVariable UUID idVoluntario){
        return pedidosService.listarPedidosVoluntarioById(idVoluntario);
    }

    @RolesAllowed({"VOLUNTARIO"})
    @GetMapping("/voluntario")
    @ResponseStatus(OK)
    public List<PedidoResponse> listarPedidosVoluntario(){
        return pedidosService.listarPedidosVoluntario();

    }

    @RolesAllowed({"ADMIN_CD"})
    @GetMapping("/centro")
    @ResponseStatus(OK)
    public List<PedidoResponse> listarPedidosCentro(){
        return pedidosService.listaPedidosCentro();
    }

    @RolesAllowed({"ADMIN_CD"})
    @PatchMapping("/atribuir-voluntario/voluntario/{id_voluntario}/pedido/{id_pedido}")
    public ResponseEntity<PedidoResponse> atribuirVoluntarioPedido(@PathVariable(value = "id_voluntario") UUID idVoluntario,
                                                                   @PathVariable(value = "id_pedido") UUID idPedido) {
        return new ResponseEntity<>(pedidosService.atribuirVoluntarioPedido(idVoluntario, idPedido), OK);
    }

    @RolesAllowed({"ADMIN_ABRIGO"})
    @PostMapping("/criar/{idDestinatario}")
    @ResponseStatus(CREATED)
    public IdResponse criarPedido(@RequestBody IncluirPedidoRequest request,@PathVariable UUID idDestinatario){
        return pedidosService.criarPedido(request, idDestinatario);
    }

    @RolesAllowed({"ADMIN_CD", "VOLUNTARIO"})
    @PostMapping("/troca-status/{id_pedido}/{status_pedido}")
    @ResponseStatus(OK)
    public PedidoResponse trocaStatus(@PathVariable UUID id_pedido, @PathVariable String status_pedido){
        return pedidosService.trocaStatus(status_pedido,id_pedido);
    }

    @RolesAllowed({"ADMIN_ABRIGO"})
    @GetMapping("listar")
    @ResponseStatus(OK)
    public List<PedidoResponse> listarPedidos(@RequestParam(required = false) StatusPedido status){
        return pedidosService.listarPedidosAbrigo(status);
    }

}
