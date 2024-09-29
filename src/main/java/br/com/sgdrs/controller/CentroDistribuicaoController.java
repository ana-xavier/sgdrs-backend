package br.com.sgdrs.controller;

import br.com.sgdrs.controller.request.CentroDistribuicaoRequest;
import br.com.sgdrs.controller.response.CentroDistribuicaoResponse;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.mapper.CentroDistribuicaoMapper;
import br.com.sgdrs.service.CentroDistribuicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/centro-distribuicao")
public class CentroDistribuicaoController {


    @Autowired
    CentroDistribuicaoService centroDistribuicaoService;


    @PostMapping("/cadastrar/centro-distribuicao")
    public ResponseEntity<CentroDistribuicaoResponse> cadastrarCentroDistribuicao(@RequestBody CentroDistribuicaoRequest centroDistribuicaoRequest) {
        CentroDistribuicao centroDistribuicao = CentroDistribuicaoMapper.toEntity(centroDistribuicaoRequest);

        Optional<CentroDistribuicao> optionalCentroDistribuicao = centroDistribuicaoService.salvarCentroDistribuicao(centroDistribuicao);

        if (optionalCentroDistribuicao.isPresent()) {
            return new ResponseEntity<>(CentroDistribuicaoMapper.toResponse(optionalCentroDistribuicao.get()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
