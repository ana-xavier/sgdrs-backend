package br.com.sgdrs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgdrs.domain.Pedido;


public interface PedidoRepository extends JpaRepository<Pedido,UUID> {  
}