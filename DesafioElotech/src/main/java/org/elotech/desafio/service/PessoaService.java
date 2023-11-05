package org.elotech.desafio.service;

import java.time.LocalDate;

import org.elotech.desafio.exception.BadRequestException;
import org.elotech.desafio.model.Contato;
import org.elotech.desafio.model.Pessoa;
import org.elotech.desafio.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PessoaService {
	
	private PessoaRepository repository;
	
	private ContatoService contatoService;
	
	public Pessoa salvarPessoa (Pessoa pessoa) {
		pessoa.setCpf(limparCpf(pessoa.getCpf()));
		validar(pessoa);
		return repository.save(pessoa);
	}
	
	public String limparCpf(String cpf) {
		cpf = cpf.replace(".","").replace("-", "");
		return cpf;
	}
	
	
	public void validar(Pessoa pessoa) {
		if (pessoa.getDataNascimento().isAfter(LocalDate.now())) {
            throw new BadRequestException("A data de nascimento não pode ser uma data futura");
        }
		
		if(pessoa.getContatosList().isEmpty()) {
            throw new BadRequestException("A pessoa deve possuir ao menos um contato");
		}
		
		if (!cpfValido(pessoa.getCpf())) {
			throw new BadRequestException("CPF inválido");
		}
		
//		if(pessoa.getCpf().isBlank() || pessoa.getNome().isBlank() || pessoa.getDataNascimento().toString().isBlank() ) {
//			throw new BadRequestException("Todos os campos de pessoa são obrigatórios");
//		}
		
		for(Contato contato : pessoa.getContatosList()) {
		    
			if(contato.getNome().isBlank() ||contato.getTelefone().isBlank() || contato.getEmail().isBlank()) {
	            throw new BadRequestException("Campos em Brancos ou nulos não são permitidos nos Contatos!"); 
			}
			System.out.println("Contato" + contato);
			System.out.println("Validating email: " + contato.getEmail());
			if(!(contatoService.emailValido(contato.getEmail()))) {
				throw new BadRequestException("Email inválido");
			}	
		}
		
	}

	public boolean cpfValido(String cpf) {
		
		if(cpf.length()!=11 || cpf.chars().allMatch(x -> x == cpf.charAt(0))) {
			
			return false;
		}
		
		String cpfSemDigitoVerificador = cpf.substring(0, 9);
		
		Integer sum = 0, digitoVerificador1, digitoVerificador2;
		
		for(int i=0; i<cpfSemDigitoVerificador.length();i++) {
		
			int cpfNum = Integer.parseInt(String.valueOf(cpfSemDigitoVerificador.charAt(i))) ;
			
			sum += cpfNum * (10-i);
		}
		
		if(sum % 11 <2) {
			
			digitoVerificador1 = 0;
		
		}else {
			
			digitoVerificador1 = 11 - (sum % 11);
		}
		
		if(!cpf.substring(0, 10).equals(cpfSemDigitoVerificador.concat(digitoVerificador1.toString()))) {
			
			return false;
		}
		
		String cpfDigitoVerificador1 = cpf.substring(0,10);
		
		sum=0;
	
		for(int i=0; i<cpfDigitoVerificador1.length();i++) {
			
			int cpfNum = Integer.parseInt(String.valueOf(cpfDigitoVerificador1.charAt(i))) ;
			
			sum += cpfNum * (11-i);
		}
		
		if(sum % 11 <2) {
			
			digitoVerificador2 = 0;
			
		}else {
			
			digitoVerificador2 = 11 - (sum % 11);
		}
		
		if(!cpf.equals(cpfDigitoVerificador1.concat(digitoVerificador2.toString()))) {
			return false;
		}
		
		return true;
	}
	

}
