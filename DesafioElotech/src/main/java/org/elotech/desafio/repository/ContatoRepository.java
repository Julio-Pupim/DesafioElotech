package org.elotech.desafio.repository;

import org.elotech.desafio.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Long>{

}
