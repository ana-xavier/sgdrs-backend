package br.com.sgdrs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgdrs.domain.ProdutoDoacao;

public interface ProdutoDoacaoRepository extends JpaRepository<ProdutoDoacao,UUID>{
    
}
