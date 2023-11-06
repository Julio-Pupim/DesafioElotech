package org.elotech.desafio.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.elotech.desafio.dto.PessoaRecordDTO;
import org.elotech.desafio.model.Contato;
import org.elotech.desafio.model.Pessoa;
import org.elotech.desafio.repository.PessoaRepository;
import org.elotech.desafio.service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PessoaEndpoint.class)
public class PessoaEndpointTest {
	
	@MockBean
	private PessoaService service;
	
	@MockBean
	private PessoaRepository repository;	
	
	@Autowired
	private MockMvc mockMvc;
	
	private Pessoa pessoa;
	
	private Contato contato;
	
	@BeforeEach
	void setUp() {
		contato = new Contato();
		contato.setEmail("teste@gmail.com");
		contato.setNome("Nome teste Junior");
		contato.setTelefone("123456789");
		pessoa = new Pessoa(1L,"nome teste","10739909908",LocalDate.of(2001,4,11),Collections.singletonList(contato));
	}
	
	@Test
	public void buscarPessoasTest() throws Exception {
		
		String nome = "";
		int page = 0;
		int size = 10;
		Pageable pageable = PageRequest.of(page, size);
		List<Pessoa> pessoas = Arrays.asList(pessoa);
		Page<Pessoa> pessoaPage = new PageImpl<Pessoa>(pessoas, pageable, pessoas.size());

		when(repository.findByNome(nome, pageable)).thenReturn(pessoaPage);
	
		mockMvc.perform(get("/pessoas")
				.param("nome", nome)
				.param("page", String.valueOf(page))
				.param("size", String.valueOf(size)))
				.andExpect(status().isOk());
		
		verify(repository, times(1)).findByNome(nome, pageable);

	}
	@Test
	public void buscarPessoasSemConteudoTest() throws Exception {
		
		String nome = "";
		int page = 0;
		int size = 10;
		Pageable pageable = PageRequest.of(page, size);
		Page<Pessoa> pessoaPage = Page.empty(pageable);
		
		when(repository.findByNome(nome, pageable)).thenReturn(pessoaPage);
		
		mockMvc.perform(get ("/pessoas")
	            .param("nome",nome)
	            .param("page",String.valueOf(page))
	            .param("size",String.valueOf(size)))
	            .andExpect(status().isNoContent());
	    
	    verify(repository,times(1)).findByNome(nome, pageable);
	}
	
	@Test
	public void buscarUmaPessoaTest() throws Exception{
		
		when(repository.findById(1L)).thenReturn(Optional.of(pessoa));
		
		mockMvc.perform(get("/pessoas/{id}",1L))
		.andExpect(status().isOk());
		
		verify(repository, times(1)).findById(1l);
		
	}
	@Test
	public void buscarUmaPessoaInexistente() throws Exception{
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		mockMvc.perform(get("/pessoas/{id}",1L))
		.andExpect(status().isNotFound());
		
		verify(repository, times(1)).findById(1l);
	}
	
	@Test
	public void salvarPessoaTest() throws Exception{
	    PessoaRecordDTO pessoaRecord = new PessoaRecordDTO("nome teste","10739909908",LocalDate.of(2001, 4, 11),Collections.singletonList(contato));
	    
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
	    
	    when(service.salvarPessoa(any(Pessoa.class))).thenReturn(pessoa);
	    
	    mockMvc.perform(post("/pessoas")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(pessoaRecord)))
	            .andExpect(status().isCreated());
	    
	    verify(service,times(1)).salvarPessoa(any(Pessoa.class));
	}
	
	@Test
	public void salvarPessoaComDadosInvalidosTest() throws Exception{
		
		PessoaRecordDTO pessoaRecord = new PessoaRecordDTO("","",null,null);
		
		ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
		
		mockMvc.perform(post("/pessoas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(pessoaRecord)))
	            .andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void atualizarPessoaTest() throws Exception{
	   
		PessoaRecordDTO pessoaRecord = new PessoaRecordDTO("nome teste","10739909908",LocalDate.of(2001, 4, 11),Collections.singletonList(contato));
	    
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
	    
	    when(repository.findById(1L)).thenReturn(Optional.of(pessoa));
	    when(service.salvarPessoa(any(Pessoa.class))).thenReturn(pessoa);
	    
	    mockMvc.perform(put("/pessoas/{id}",1L)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(pessoaRecord)))
	            .andExpect(status().isOk());
	    
	    verify(repository,times(1)).findById(1L);
	    verify(service,times(1)).salvarPessoa(any(Pessoa.class));
	}
	@Test
	public void atualizarPessoaComDadosInvalidos() throws Exception{
	   
		PessoaRecordDTO pessoaRecord = new PessoaRecordDTO("","",null,null);
	    
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
	    
	    when(repository.findById(1L)).thenReturn(Optional.empty());
	    
	    mockMvc.perform(put("/pessoas/{id}",1L)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(mapper.writeValueAsString(pessoaRecord)))
	    		.andExpectAll(status().isBadRequest());
	}
	@Test
	public void atualizarPessoaInexistenteTest() throws Exception{
	   
		PessoaRecordDTO pessoaRecord = new PessoaRecordDTO("nome teste","10739909908",LocalDate.of(2001, 4, 11),Collections.singletonList(contato));
	    
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
	    
	    when(repository.findById(1L)).thenReturn(Optional.empty());
	    
	    mockMvc.perform(put("/pessoas/{id}",1L)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(mapper.writeValueAsString(pessoaRecord)))
				.andExpect(status().isNotFound());
	    
	    verify(repository,times(1)).findById(1L);
	    verify(service, times(0)).salvarPessoa(any(Pessoa.class));

	}
	
	@Test
	public void deletarPessoaTest() throws Exception{
	   
		when(repository.findById(1L)).thenReturn(Optional.of(pessoa));
		
	    mockMvc.perform(delete("/pessoas/{id}",1L))
				.andExpect(status().isOk());
	    
	    
	    verify(repository, times(1)).deleteById(1L);

	}
	@Test
	public void deletarPessoaInexistente() throws Exception{
		
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		mockMvc.perform(delete("/pessoas/{id}",1L))
				.andExpect(status().isNotFound());
		
	    verify(repository, times(0)).deleteById(1L);
	}
}
