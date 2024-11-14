package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.EstoqueRequest;
import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.controller.response.ItemVerificadoResponse;
import br.com.sgdrs.service.EstoqueService;
import jakarta.annotation.security.RolesAllowed;
import pl.coderion.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("estoque")
public class EstoqueController {
    @Autowired
    private EstoqueService estoqueService;

    @RolesAllowed({"VOLUNTARIO"})
    @GetMapping("/verificar/{codigoProduto}")
    @ResponseStatus(OK)
    public ItemVerificadoResponse verificarCadastrarProduto(@PathVariable String codigoProduto){
        return estoqueService.verificar(codigoProduto);
    }

    @RolesAllowed({"VOLUNTARIO"})
    @PatchMapping("/cadastrar-itens")
    @ResponseStatus(OK)
    public List<ItemResponse> cadastrarItens(@RequestBody EstoqueRequest request){
        return estoqueService.cadastrarItens(request);
    }

    @RolesAllowed({"VOLUNTARIO"})
    @DeleteMapping("/remover-itens/{id_pedido}/")
    @ResponseStatus(OK)
    public List<ItemResponse> removerItens(@PathVariable(value = "id_pedido") UUID idPedido,
                                           @RequestBody EstoqueRequest estoqueRequest){
        return estoqueService.removerItens(estoqueRequest);
    }
}
