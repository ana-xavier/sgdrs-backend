package br.com.sgdrs.controller;

import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.dto.CentroDistribuicaoDTO;
import br.com.sgdrs.service.EstoqueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/estoque")
public class EstoqueController {

    @Autowired
    EstoqueService estoqueService;

    @PostMapping("/cadastrar/centro-distribuicao")
    public ResponseEntity<CentroDistribuicao> cadastrarCentroDistribuicao(@RequestBody CentroDistribuicaoDTO centroDistribuicaoDTO) {
        CentroDistribuicao centroDistribuicao = CentroDistribuicao.builder().build();
        BeanUtils.copyProperties(centroDistribuicaoDTO, centroDistribuicao);

        Optional<CentroDistribuicao> optionalCentroDistribuicao = estoqueService.save(centroDistribuicao);

        if (optionalCentroDistribuicao.isPresent()) {
            return new ResponseEntity<>(optionalCentroDistribuicao.get(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
