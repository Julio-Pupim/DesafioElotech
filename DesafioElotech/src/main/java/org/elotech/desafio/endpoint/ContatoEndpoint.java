package org.elotech.desafio.endpoint;

import java.util.Optional;

import org.elotech.desafio.dto.ContatoRecordDTO;
import org.elotech.desafio.model.Contato;
import org.elotech.desafio.repository.ContatoRepository;
import org.elotech.desafio.service.ContatoService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/contatos")
@AllArgsConstructor
public class ContatoEndpoint {
	
	private final ContatoService service;
	private final ContatoRepository repository;
	
	
	@GetMapping
	public ResponseEntity<Page<Contato>> buscarContatos(@RequestParam(defaultValue= "")String nome,
			  											@RequestParam(defaultValue = "0") int page,
			  											@RequestParam(defaultValue = "10") int size){
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Contato> contatoPage;
		
		if(nome.isEmpty()) {
			contatoPage = repository.findAll(pageable);
		}else {
			contatoPage = repository.findByNome(nome, pageable);
		}
		if(contatoPage.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(contatoPage);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(contatoPage);
	} 
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> buscarContato(@PathVariable(value = "id")Long id){
		
		Optional<Contato> contatoOptional = repository.findById(id);
		
		if(contatoOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato não encontrado");

		}
		return ResponseEntity.status(HttpStatus.OK).body(contatoOptional.get());
	}
	
	@PostMapping
	public ResponseEntity<Contato> salvarContato(@RequestBody @Valid ContatoRecordDTO contatoRecord){
		Contato contato = new Contato();
		BeanUtils.copyProperties(contatoRecord, contato);
		Contato contatoSalvo = service.salvarContato(contato);
		return ResponseEntity.status(HttpStatus.CREATED).body(contatoSalvo);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> atualizarContato(@PathVariable(value="id")Long id, @RequestBody @Valid ContatoRecordDTO contatoRecord){
		Optional<Contato> contatoOptional = repository.findById(id);
		if(contatoOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato não encontrado");
		}
		Contato contato = contatoOptional.get();
		BeanUtils.copyProperties(contatoRecord,contato);
		Contato contatoSalvo = service.salvarContato(contato);
		return ResponseEntity.status(HttpStatus.OK).body(contatoSalvo);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletarContato(@PathVariable(value="id")Long id){
		Optional<Contato> contato = repository.findById(id);
		if(contato.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato não encontrado");
		}
		repository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body("Contato deletado");
	}
}
