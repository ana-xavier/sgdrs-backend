package br.com.sgdrs.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Item;

public interface ItemRepository extends JpaRepository<Item, UUID> { 
    Optional<Item> findByCodBarrasAndCentroDistribuicao(String CodBarras,CentroDistribuicao centroDistribuicao);
}
