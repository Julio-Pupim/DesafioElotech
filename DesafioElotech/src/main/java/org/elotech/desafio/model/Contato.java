package org.elotech.desafio.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="contato")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "pessoa")
public class Contato implements Serializable{
	
	private static final long serialVersionUID = 8681104137432506922L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contato_idkey_seq")
    @SequenceGenerator(name = "contato_idkey_seq", sequenceName = "contato_idkey_seq", allocationSize = 1)
	@Column(name="idkey")
	private Long id;
	
	private String nome;
	
	private String telefone;
	
	private String email;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "idkey_pessoa",referencedColumnName = "idkey")
	private Pessoa pessoa;
}
