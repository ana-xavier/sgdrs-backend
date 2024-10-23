package br.com.sgdrs.repository;

import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Pedido;
import br.com.sgdrs.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    List<Pedido> findByVoluntario(Usuario voluntario);
    List<Pedido> findByCentroDistribuicao(CentroDistribuicao centroDistribuicao);
}