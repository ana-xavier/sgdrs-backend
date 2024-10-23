package br.com.sgdrs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sgdrs.domain.Item;

public interface ItemRepository extends JpaRepository<Item,UUID> {  
}