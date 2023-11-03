package org.elotech.desafio.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "pessoa")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pessoa implements Serializable{
	
	private static final long serialVersionUID = 5779762933177727652L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_idkey_seq")
    @SequenceGenerator(name = "pessoa_idkey_seq", sequenceName = "pessoa_idkey_seq",allocationSize = 1)
	@Column(name = "idkey")
	private Long id;
	
	private String nome;
	
	private String cpf;
	
	@Column(name="datanascimento")
	private LocalDate dataNascimento;
	
	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contato> contatosList = new ArrayList<>();
	
}
