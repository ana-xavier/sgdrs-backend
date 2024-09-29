package br.com.sgdrs.service;

import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.repository.CentroDistribuicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CentroDistribuicaoService {

    @Autowired
    CentroDistribuicaoRepository centroDistribuicaoRepository;

    public Optional<CentroDistribuicao> salvarCentroDistribuicao(CentroDistribuicao centroDistribuicao) {
        return Optional.ofNullable(centroDistribuicaoRepository.save(centroDistribuicao));
    }

    public List<CentroDistribuicao> listarCentrosDistribuicao() {
        return centroDistribuicaoRepository.findAll();
    }
}
