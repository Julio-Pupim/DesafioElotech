package org.elotech.desafio.repository;

import org.elotech.desafio.model.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ContatoRepository extends JpaRepository<Contato, Long>{
	
	Page<Contato> findByNome(String nome, Pageable pageable);
	Page<Contato> findAll(Pageable pageable);

}
