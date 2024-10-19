package br.com.sgdrs.controller;

import br.com.sgdrs.controller.response.PedidoResponse;
import br.com.sgdrs.service.PedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

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
}
