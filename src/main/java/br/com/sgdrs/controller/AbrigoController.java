package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.IncluirAbrigoRequest;
import br.com.sgdrs.controller.request.IncluirPedidoRequest;
import br.com.sgdrs.controller.response.AbrigoResponse;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.domain.Abrigo;
import br.com.sgdrs.service.AbrigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/abrigos")
public class AbrigoController {

    @Autowired
    private AbrigoService abrigoService;

    @GetMapping("/listar")
    @ResponseStatus(OK)
    public List<AbrigoResponse> listar(){
        return abrigoService.listar();
    }

    @GetMapping("/listarItens")
    @ResponseStatus(OK)
    public List<ItemResponse> listarItens(){
        return abrigoService.listarItens();
    }

    @PostMapping("/criar/{idCriador}")
    @ResponseStatus(CREATED)
    public IdResponse criar(@RequestBody IncluirAbrigoRequest request, @PathVariable UUID idCriador){
        return abrigoService.criar(request, idCriador);
    }

    @PostMapping("/criar-pedido/{idCriador}/{idDestinatario}")
    @ResponseStatus(CREATED)
    public IdResponse criarPedido(@RequestBody IncluirPedidoRequest request, @PathVariable UUID idCriador,@PathVariable UUID idDestinatario){
        return abrigoService.criarPedido(request, idCriador,idDestinatario);
    }
}
