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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.elotech.desafio.dto.ContatoRecordDTO;
import org.elotech.desafio.model.Contato;
import org.elotech.desafio.model.Pessoa;
import org.elotech.desafio.repository.ContatoRepository;
import org.elotech.desafio.service.ContatoService;
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

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContatoEndpoint.class)
public class ContatoEndpointTest {
	
	@MockBean
	private ContatoService service;
	
	@MockBean 
	private ContatoRepository repository;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Contato contato;
	
	@BeforeEach
	public void setUp() {
		contato = new Contato();
		contato.setId(1L);
		contato.setNome("Nome Teste");
		contato.setEmail("teste@gmail.com");
		contato.setTelefone("123456789");
	}
	
	@Test
	public void buscarContatosComNomeTest() throws Exception {
		
		String nome = "Nome Test";
		int page = 0;
		int size = 10;
		Pageable pageable = PageRequest.of(page, size);
		List<Contato> contatos = Arrays.asList(contato);
		Page<Contato> contatoPage = new PageImpl<Contato>(contatos,pageable,contatos.size());
		
		when(repository.findByNome(nome, pageable)).thenReturn(contatoPage);
		
		mockMvc.perform(get ("/contatos")
				.param("nome",nome)
				.param("page",String.valueOf(page))
				.param("size",String.valueOf(size)))
				.andExpect(status().isOk());
		
		verify(repository,times(1)).findByNome(nome, pageable);
	}
	@Test
	public void buscarContatosSemNomeTest() throws Exception {
		
		String nome = "";
		int page = 0;
		int size = 10;
		Pageable pageable = PageRequest.of(page, size);
		List<Contato> contatos = Arrays.asList(contato);
		Page<Contato> contatoPage = new PageImpl<Contato>(contatos,pageable,contatos.size());
		
		when(repository.findAll(pageable)).thenReturn(contatoPage);
		
		mockMvc.perform(get ("/contatos")
				.param("nome",nome)
				.param("page",String.valueOf(page))
				.param("size",String.valueOf(size)))
				.andExpect(status().isOk());
		
		verify(repository,times(1)).findAll(pageable);
	}
	@Test
	public void buscarContatosSemConteudoSemNomeTest()throws Exception{
		
		String nome = "";
		int page = 0;
		int size = 10;
	    Pageable pageable = PageRequest.of(page, size);
	    Page<Contato> contatoPage = Page.empty(pageable);
	    
	    when(repository.findAll(pageable)).thenReturn(contatoPage);
	    
	    mockMvc.perform(get ("/contatos")
	            .param("nome",nome)
	            .param("page",String.valueOf(page))
	            .param("size",String.valueOf(size)))
	            .andExpect(status().isNoContent());
	    
	    verify(repository,times(1)).findAll(pageable);
	}
	@Test
	public void buscarContatosSemConteudoComNomeTest()throws Exception{
		
		String nome = "Nome Teste";
		int page = 0;
		int size = 10;
	    Pageable pageable = PageRequest.of(page, size);
	    Page<Contato> contatoPage = Page.empty(pageable);
	    
	    when(repository.findByNome(nome, pageable)).thenReturn(contatoPage);
	    
	    mockMvc.perform(get ("/contatos")
	            .param("nome",nome)
	            .param("page",String.valueOf(page))
	            .param("size",String.valueOf(size)))
	            .andExpect(status().isNoContent());
	    
	    verify(repository,times(1)).findByNome(nome, pageable);
	}
	
	
	@Test
	public void buscarUmContatoTest() throws Exception{
		
		when(repository.findById(1L)).thenReturn(Optional.of(contato));
		
		mockMvc.perform(get("/contatos/{id}",1L))
				.andExpect(status().isOk());
		
		verify(repository, times(1)).findById(1l);

	}
	@Test
	public void buscarUmContatoInexistenteTest() throws Exception{
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		mockMvc.perform(get("/contatos/{id}",1L))
		.andExpect(status().isNotFound());
		
		verify(repository, times(1)).findById(1l);

	}
	
	@Test
	public void salvarContatoTest() throws Exception{
		
		ContatoRecordDTO contatoRecord = new ContatoRecordDTO("nome teste","123456789","teste@gmail.com",new Pessoa());
		
		when(service.salvarContato(any(Contato.class))).thenReturn(contato);
		
		mockMvc.perform(post("/contatos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(contatoRecord)))
				.andExpect(status().isCreated());
		
		verify(service,times(1)).salvarContato(any(Contato.class));
	}
	
	@Test
	public void salvarContatoComDadosInvalidsoTest() throws Exception{
		
		ContatoRecordDTO contatoRecord = new ContatoRecordDTO("","","",null);
		
		mockMvc.perform(post("/contatos")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(contatoRecord)))
	            .andExpect(status().isBadRequest());
		
	} 
	
	@Test
	public void atualizarContatoTest() throws Exception{
		
		ContatoRecordDTO contatoRecord = new ContatoRecordDTO("nome teste","123456789","teste@gmail.com",new Pessoa());
		
		when(repository.findById(1L)).thenReturn(Optional.of(contato));	
		when(service.salvarContato(any(Contato.class))).thenReturn(contato);
		
		mockMvc.perform(put("/contatos/{id}",1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(contatoRecord)))
				.andExpect(status().isOk());
		
		   verify(repository,times(1)).findById(1L);
		    verify(service,times(1)).salvarContato(any(Contato.class));
	}
	@Test
	public void atualizarContatoInexistenteTest() throws Exception{
		 
		ContatoRecordDTO contatoRecord = new ContatoRecordDTO("nome teste","123456789","teste@gmail.com",new Pessoa());

		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		mockMvc.perform(put("/contatos/{id}",1L)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(contatoRecord)))
	            .andExpect(status().isNotFound());

		verify(repository,times(1)).findById(1L);
}
	
	@Test
	public void atualizarContatoComDadosInvalidosTest() throws Exception{		
		ContatoRecordDTO contatoRecord = new ContatoRecordDTO("","","",null);
		
		mockMvc.perform(put("/contatos/{id}",1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(contatoRecord)))
				.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void deletarContatoTest() throws Exception{
		
		when(repository.findById(1L)).thenReturn(Optional.of(contato));
		
		mockMvc.perform(delete("/contatos/{id}",1L))
				.andExpect(status().isOk());
		
		verify(repository,times(1)).findById(1L);
	    verify(repository, times(1)).deleteById(1L);

	}
	@Test
	public void deletarContatoInexistenteTest() throws Exception{
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		mockMvc.perform(delete("/contatos/{id}",1L))
		.andExpect(status().isNotFound());
		
		verify(repository,times(1)).findById(1L);
	    verify(repository, times(0)).deleteById(1L);
	}
} 
