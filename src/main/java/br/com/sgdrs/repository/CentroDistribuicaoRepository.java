package br.com.sgdrs.repository;

import br.com.sgdrs.domain.CentroDistribuicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CentroDistribuicaoRepository extends JpaRepository<CentroDistribuicao, UUID> {
}
