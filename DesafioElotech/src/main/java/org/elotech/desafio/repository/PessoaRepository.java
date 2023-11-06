package org.elotech.desafio.repository;

import org.elotech.desafio.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	
	Page<Pessoa> findByNome(String nome, Pageable pageable);
}
