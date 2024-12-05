package br.com.sgdrs.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.management.relation.Relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.sgdrs.domain.Doador;
import br.com.sgdrs.domain.Relatorio.Relatorio;

public interface DoadorRepository extends JpaRepository<Doador, UUID> {

    Optional<Doador> findByCpfCnpj(String cpfCnpjDoador);

    @Query(value = """
            SELECT new br.com.sgdrs.domain.Relatorio.Relatorio(
                d.nome,
                d.cpfCnpj,
                i.nome,
                i.categoria,
                i.valorMedida,
                i.unidadeMedida,
                i.codBarras,
                pd.quantidade,
                doa.data
            )
            FROM 
                Doador d
            JOIN 
                d.doacoes doa
            JOIN 
                doa.produtosDoacao pd
            JOIN 
                pd.item i
            WHERE 
                i.centroDistribuicao.id = :id_centro
            """)
List<Relatorio> findRelatorioByCentroId(@Param("id_centro") UUID id_centro);

}
