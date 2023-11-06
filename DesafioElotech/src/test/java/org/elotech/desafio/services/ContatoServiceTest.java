package org.elotech.desafio.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.elotech.desafio.repository.ContatoRepository;
import org.elotech.desafio.service.ContatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContatoServiceTest {

	@InjectMocks
	private ContatoService service;
	
	@Mock
	private ContatoRepository repository;

	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
		
	
	@Test
	public void DeveTestarEmailValido() {
		
		assertTrue(service.emailValido("teste@gmail.com"));
		assertTrue(service.emailValido("1234teste@gmail.com"));
		assertTrue(service.emailValido("teste1234@gmail.com"));
		assertTrue(service.emailValido("teste@outlook.com"));

	}
	@Test
	public void naoDevePermitirEmailInvalido() {
		
		assertFalse(service.emailValido("@gmail.com"));
		assertFalse(service.emailValido("testegmail.com"));
		assertFalse(service.emailValido("teste@gmail"));
		assertFalse(service.emailValido("teste@gmail@.com"));
		assertFalse(service.emailValido("testegmail.com@"));
	}
}
