package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.EstoqueRequest;
import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.controller.response.ItemVerificadoResponse;
import br.com.sgdrs.service.EstoqueService;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

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

    @RolesAllowed({"ADMIN_CD"})
    @GetMapping("/listarItens")
    @ResponseStatus(OK)
    public List<ItemResponse> listarItensNaoValidados(){
        return estoqueService.listarItensNaoValidados();
    }
}
