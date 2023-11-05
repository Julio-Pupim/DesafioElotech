package org.elotech.desafio.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.elotech.desafio.model.Contato;
import org.elotech.desafio.repository.ContatoRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContatoService {

	private final ContatoRepository contatoRepository;
	
	
	   public Contato salvar (Contato contato) {
	        validar(contato);
	        return contatoRepository.save(contato);
	    }

	    public void validar(Contato contato) {
	        if (!emailValido(contato.getEmail())) {
	            throw new IllegalArgumentException("Email inválido");
	        }
	    	if(contato.getNome().isBlank() ||contato.getTelefone().isBlank() || contato.getEmail().isBlank()) {
	            throw new IllegalArgumentException("Campos em Brancos ou nulos não são permitidos nos Contatos!"); 
			}
	    }
	    
		    public Boolean emailValido(String email) {
		        String regex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.([A-Za-z]{2,4})$";
		    	Pattern pattern = Pattern.compile(regex);
		    	Matcher matcher = pattern.matcher(email);
		    	return matcher.matches();
		    }
}
