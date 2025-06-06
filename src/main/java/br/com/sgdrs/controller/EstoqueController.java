package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.EditarItemRequest;
import br.com.sgdrs.controller.request.EstoqueRequest;
import br.com.sgdrs.controller.response.EditarItemResponse;
import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.controller.response.ItemVerificadoResponse;
import br.com.sgdrs.controller.response.RelatorioResponse;
import br.com.sgdrs.domain.Doador;
import br.com.sgdrs.service.EstoqueService;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("estoque")
public class EstoqueController {
    @Autowired
    private EstoqueService estoqueService;

    @RolesAllowed({"VOLUNTARIO"})
    @GetMapping("/verificar")
    @ResponseStatus(OK)
    public ItemVerificadoResponse verificarCadastrarProduto(@RequestParam(name = "codigoProduto", required = false) String codigoProduto){
        return estoqueService.verificar(codigoProduto);
    }

    @RolesAllowed({"VOLUNTARIO"})
    @PatchMapping("/cadastrar-itens")
    @ResponseStatus(OK)
    public List<ItemResponse> cadastrarItens(@RequestBody EstoqueRequest request){
        return estoqueService.cadastrarItens(request);
    }

    @RolesAllowed({"ADMIN_CD"})
    @GetMapping("/listarItensInvalidos")
    @ResponseStatus(OK)
    public List<ItemResponse> listarItensNaoValidados(){
        return estoqueService.listarItensNaoValidados();
    }

    @RolesAllowed({"VOLUNTARIO"})
    @PatchMapping("/editarItem/{idItem}")
    @ResponseStatus(OK)
    public EditarItemResponse editarItem(@PathVariable UUID idItem, @RequestBody EditarItemRequest request){
        return estoqueService.editarItem(idItem, request);
    }

    @RolesAllowed({"ADMIN_CD"})
    @PatchMapping("/aprovarItem/{idItem}/{idDoador}")
    @ResponseStatus(OK)
    public EditarItemResponse aprovarItem(@PathVariable UUID idItem, @PathVariable UUID idDoador, @RequestBody EditarItemRequest request){
        return estoqueService.aprovarItem(idItem, idDoador, request);
    }

    @RolesAllowed({"VOLUNTARIO"})
    @PostMapping("/incluirDoador")
    @ResponseStatus(OK)
    public Doador incluirDoador(@RequestBody(required = false) Doador request){
        return estoqueService.IncluirDoador(request);
    }

    @RolesAllowed({"ADMIN_CD"})
    @GetMapping("/exportarRelatorioDoacao/{id_centro}")
    @ResponseStatus(OK)
    public RelatorioResponse incluirDoador(@PathVariable UUID id_centro) throws IOException{
        return estoqueService.exportarRelatorioDoacao(id_centro);
    }




}
