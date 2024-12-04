package br.com.sgdrs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgdrs.domain.Doacao;

public interface DoacaoRepository extends JpaRepository<Doacao,UUID> {

}
