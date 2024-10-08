package br.com.sgdrs.repository;

import br.com.sgdrs.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {
    @Query(value = "SELECT * " +
            "FROM endereco e " +
            "WHERE e.cep = :cep " +
            "AND e.logradouro = :logradouro " +
            "AND e.numero = :numero " +
            "AND e.cidade = :cidade " +
            "AND e.estado = :estado", nativeQuery = true)
    Optional<Endereco> findUniqueAddress(
            @Param("cep") String cep,
            @Param("logradouro") String logradouro,
            @Param("numero") String numero,
            @Param("cidade") String cidade,
            @Param("estado") String estado
    );
}
