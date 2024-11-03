package br.com.sgdrs.repository;

import br.com.sgdrs.domain.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissaoRepository extends JpaRepository<Permissao, UUID> {
}
