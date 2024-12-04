package br.com.sgdrs.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, UUID> { 
    Optional<Item> findByCodBarrasAndCentroDistribuicao(String CodBarras,CentroDistribuicao centroDistribuicao);
    List<Item> findByValidado(boolean validado);
    Optional<Item> findByIdAndCentroDistribuicao(UUID id,CentroDistribuicao centroDistribuicao);
    List<Item> findByValidadoAndCentroDistribuicao(boolean validado, CentroDistribuicao centroDistribuicao);

    @Query("SELECT i FROM Item i WHERE i.nome = :nome AND i.categoria = :categoria AND i.id != :id")
    Optional<Item> encontrarPorNomeCategoriaEIdDiferente(@Param("nome") String nome, @Param("categoria") String categoria, @Param("id") UUID id);

}
