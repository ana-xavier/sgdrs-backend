package br.com.sgdrs.controller;

import br.com.sgdrs.service.EstoqueService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("estoques")
public class EstoqueController {
    @Autowired
    private EstoqueService estoqueService;

    @RolesAllowed({"VOLUNTARIO"})
    @GetMapping("/verificar/{codigoProduto}")
    @ResponseStatus(OK)
    public void verificarCadastrarProduto(@PathVariable String codigoProduto){
        estoqueService.verificar(codigoProduto);
    }
}
