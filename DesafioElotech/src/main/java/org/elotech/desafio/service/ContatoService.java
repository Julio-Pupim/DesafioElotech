package org.elotech.desafio.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elotech.desafio.exception.BadRequestException;
import org.elotech.desafio.model.Contato;
import org.elotech.desafio.repository.ContatoRepository;
import org.springframework.stereotype.Service;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }
	
	
	   public Contato salvarContato (Contato contato) {
	        validar(contato);
	        return contatoRepository.save(contato);
	    }

	    public void validar(Contato contato) {
	        if (!emailValido(contato.getEmail())) {
	            throw new BadRequestException("Email inválido");
	        }

	    }
	    
		    public Boolean emailValido(String email) {
		        String regex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.([A-Za-z]{2,4})$";
		    	Pattern pattern = Pattern.compile(regex);
		    	Matcher matcher = pattern.matcher(email);
		    	return matcher.matches();
		    }
}
