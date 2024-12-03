package br.com.sgdrs.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgdrs.domain.Doador;

public interface DoadorRepository  extends JpaRepository<Doador,UUID>{

    Optional<Doador>findByCpfCnpj(String cpfCnpjDoador);
    
}
