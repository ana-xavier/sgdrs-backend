package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.CentroDistribuicaoRequest;
import br.com.sgdrs.controller.request.IncluirAbrigoRequest;
import br.com.sgdrs.controller.response.AbrigoResponse;
import br.com.sgdrs.controller.response.CentroDistribuicaoResponse;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.mapper.CentroDistribuicaoMapper;
import br.com.sgdrs.mapper.IdMapper;
import br.com.sgdrs.service.CentroDistribuicaoService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
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

    @RolesAllowed({"SUPERADMIN"})
    @GetMapping("/listar")
    @ResponseStatus(OK)
    public List<CentroDistribuicaoResponse> listar(){
        return centroDistribuicaoService.listar();
    }
}
