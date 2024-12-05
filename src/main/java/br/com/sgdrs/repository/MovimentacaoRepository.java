package br.com.sgdrs.repository;

import java.util.List;
import java.util.UUID;

import br.com.sgdrs.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.sgdrs.domain.Movimentacao;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, UUID> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE movimentacao " +
                   "SET id_pedido = :id_pedido " +
                   "WHERE id_movimentacao = :id_movimentacao", nativeQuery = true)
    void updateIdPedido(
            @Param("id_pedido") UUID id_pedido,
            @Param("id_movimentacao") UUID id_movimentacao
    );

    List<Movimentacao> findByPedido(Pedido pedido);
}
