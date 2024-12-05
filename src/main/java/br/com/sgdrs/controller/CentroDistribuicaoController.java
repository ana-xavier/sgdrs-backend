package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.CentroDistribuicaoRequest;
import br.com.sgdrs.controller.response.CentroDistribuicaoResponse;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.service.CentroDistribuicaoService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/centro-distribuicao")
public class CentroDistribuicaoController {

    @Autowired
    CentroDistribuicaoService centroDistribuicaoService;


    @RolesAllowed({"SUPERADMIN"})
    @PostMapping("/criar")
    @ResponseStatus(CREATED)
    public IdResponse criar(@RequestBody CentroDistribuicaoRequest request){
        return centroDistribuicaoService.criar(request);
    }

    @RolesAllowed({"SUPERADMIN", "ADMIN_ABRIGO"})
    @GetMapping("/listar")
    @ResponseStatus(OK)
    public List<CentroDistribuicaoResponse> listar(){
        return centroDistribuicaoService.listar();
    }
}
