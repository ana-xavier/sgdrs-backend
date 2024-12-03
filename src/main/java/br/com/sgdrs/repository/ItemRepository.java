package br.com.sgdrs.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Item;

public interface ItemRepository extends JpaRepository<Item, UUID> { 
    Optional<Item> findByCodBarrasAndCentroDistribuicao(String CodBarras,CentroDistribuicao centroDistribuicao);
    List<Item> findByValidado(boolean validado);
    Optional<Item> findByIdAndCentroDistribuicao(UUID id,CentroDistribuicao centroDistribuicao);
    List<Item> findByValidadoAndCentroDistribuicao(boolean validado, CentroDistribuicao centroDistribuicao);

    Optional<Item> findByNomeAndCategoria(String nome, String categoria);
}
