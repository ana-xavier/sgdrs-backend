package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.IncluirAbrigoRequest;
import br.com.sgdrs.controller.response.AbrigoResponse;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.controller.response.ItemResponse;
import br.com.sgdrs.service.AbrigoService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RolesAllowed({"SUPERADMIN"})
    @GetMapping("/listar")
    @ResponseStatus(OK)
    public List<AbrigoResponse> listar(){
        return abrigoService.listar();
    }

    @RolesAllowed({"ADMIN_ABRIGO"})
    @GetMapping("/listarItens/{id_cd}")
    @ResponseStatus(OK)
    public List<ItemResponse> listarItens(@PathVariable UUID id_cd){
        return abrigoService.listarItens(id_cd);
    }

    @RolesAllowed({"SUPERADMIN"})
    @PostMapping("/criar")
    @ResponseStatus(CREATED)
    public IdResponse criar(@RequestBody IncluirAbrigoRequest request){
        return abrigoService.criar(request);
    }

    
}
