package br.com.sgdrs.service;

import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.repository.CentroDistribuicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    CentroDistribuicaoRepository centroDistribuicaoRepository;

    public Optional<CentroDistribuicao> save(CentroDistribuicao centroDistribuicao) {
        return Optional.ofNullable(centroDistribuicaoRepository.save(centroDistribuicao));
    }
}
