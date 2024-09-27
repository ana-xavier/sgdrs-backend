package br.com.sgdrs.repository;

import br.com.sgdrs.domain.Abrigo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AbrigoRepository extends JpaRepository<Abrigo, UUID> {

}
