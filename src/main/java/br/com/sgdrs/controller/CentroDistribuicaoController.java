package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.CentroDistribuicaoRequest;
import br.com.sgdrs.controller.request.IncluirAbrigoRequest;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.mapper.CentroDistribuicaoMapper;
import br.com.sgdrs.mapper.IdMapper;
import br.com.sgdrs.service.CentroDistribuicaoService;
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


   @PostMapping("/criar/{idCriador}")
    @ResponseStatus(CREATED)
    public IdResponse criar(@RequestBody CentroDistribuicaoRequest request, @PathVariable UUID idCriador){
        return centroDistribuicaoService.criar(request, idCriador);
    }

    @GetMapping("/listar")
    @ResponseStatus(OK)
    public ResponseEntity<List<CentroDistribuicao>> listarCentrosDistribuicao() {
        return new ResponseEntity<>(centroDistribuicaoService.listarCentrosDistribuicao(), HttpStatus.OK);
    }
}
