package org.elotech.desafio.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;

import org.elotech.desafio.exception.BadRequestException;
import org.elotech.desafio.model.Contato;
import org.elotech.desafio.model.Pessoa;
import org.elotech.desafio.repository.PessoaRepository;
import org.elotech.desafio.service.ContatoService;
import org.elotech.desafio.service.PessoaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {
	
	@InjectMocks
	private PessoaService service;
	
	@Mock
	private PessoaRepository repository;
	
	@Mock
	private ContatoService contatoService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	   
	@Test
	public void DeveTirarPontosETracosDoCpf() {
		String cpf1 = "175.705.230-59";
		cpf1 = service.limparCpf(cpf1);
		
		assertEquals("17570523059",cpf1);
		
	}
	@Test
	public void NaoDevePermitirCadastroComDataDeNascimentoFutura(){
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Nome Teste");
		pessoa.setCpf("17570523059");
		pessoa.setDataNascimento(LocalDate.of(2024, 4, 11));
		Contato contato = new Contato();
		contato.setNome("Nome Teste Júnior");
		contato.setEmail("teste@gmail.com");
		contato.setTelefone("1234567890");
		contato.setPessoa(pessoa);
		pessoa.setContatosList(Collections.singletonList(contato));
		
		Assertions.assertThrows(BadRequestException.class, () -> {
		 service.salvarPessoa(pessoa);
	    });
		
	}
	
	@Test
	public void NaoDevePermitirCadastrarPessoaSemContato() {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Nome Teste");
		pessoa.setCpf("17570523059");
		pessoa.setDataNascimento(LocalDate.of(2023, 4, 11));
		
		Assertions.assertThrows(BadRequestException.class, () -> {
			 service.salvarPessoa(pessoa);
		    });
	}
	
	@Test
	public void NaoDevePermitirCpfInvalido() {
		
		assertTrue(service.cpfValido("17570523059"));
		assertTrue(service.cpfValido("10739909908"));
		
		assertFalse(service.cpfValido("1111111111"));
		assertFalse(service.cpfValido("1234567890134"));
	}
	
	@Test
	public void DeveSalvarPessoa() {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Nome Teste");
		pessoa.setCpf("17570523059");
		pessoa.setDataNascimento(LocalDate.of(2001, 4, 11));
		Contato contato = new Contato();
		contato.setNome("Nome Teste Júnior");
		contato.setEmail("teste@gmail.com");
		contato.setTelefone("1234567890");
		contato.setPessoa(pessoa);
		pessoa.setContatosList(Collections.singletonList(contato));
		
		when(repository.save(any(Pessoa.class))).thenReturn(pessoa);
		
		Pessoa pessoaSalva = service.salvarPessoa(pessoa);
		
		assertEquals(pessoa, pessoaSalva);
		
	}
}
