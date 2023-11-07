package org.elotech.desafio.endpoint;

import java.util.ArrayList;
import java.util.Optional;

import org.elotech.desafio.dto.PessoaRecordDTO;
import org.elotech.desafio.model.Pessoa;
import org.elotech.desafio.repository.PessoaRepository;
import org.elotech.desafio.service.PessoaService;
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
@RequestMapping("/pessoas")
@AllArgsConstructor
public class PessoaEndpoint {
	
	private final PessoaService service;
	
	private final PessoaRepository repository;
	
	@GetMapping
	public ResponseEntity<Page<Pessoa>> buscarPessoas(@RequestParam(defaultValue= "")String nome,
													  @RequestParam(defaultValue = "0") int page,
													  @RequestParam(defaultValue = "10") int size){
		Pageable pageable = PageRequest.of(page, size);
		Page<Pessoa> pessoaPage;
		
		if(nome.isEmpty()) {
			pessoaPage = repository.findAll(pageable);
		} else {
			pessoaPage = repository.findByNome(nome, pageable);
		}
		
		if(pessoaPage.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pessoaPage);
		}
		return ResponseEntity.status(HttpStatus.OK).body(pessoaPage);
	}
	@GetMapping("/{id}")
	public ResponseEntity<Object> buscarUmaPessoa(@PathVariable(value = "id")Long id){
		
		Optional<Pessoa> pessoaOptional = repository.findById(id);
		
		if(pessoaOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");

		}
		return ResponseEntity.status(HttpStatus.OK).body(pessoaOptional.get());
	}
	
	@PostMapping
	public ResponseEntity<Pessoa> salvarPessoa(@RequestBody @Valid PessoaRecordDTO pessoaRecord){
		Pessoa pessoa = new Pessoa();
		BeanUtils.copyProperties(pessoaRecord, pessoa);
		Pessoa pessoaSalva = service.salvarPessoa(pessoa);
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> atualizarPessoa(@PathVariable(value="id")Long id, @RequestBody @Valid PessoaRecordDTO pessoaRecord){
		Optional<Pessoa> pessoaOptional = repository.findById(id);
		if(pessoaOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
		}
		Pessoa pessoa = pessoaOptional.get();
	
		pessoa.setContatosList(new ArrayList<>(pessoa.getContatosList()));
		pessoa.getContatosList().clear();

		pessoa.getContatosList().addAll(pessoaRecord.contatosList());

		BeanUtils.copyProperties(pessoaRecord, pessoa, "contatosList");

		Pessoa pessoaSalva = service.salvarPessoa(pessoa);
		return ResponseEntity.status(HttpStatus.OK).body(pessoaSalva);
	}	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletarPessoa(@PathVariable(value="id")Long id){
		Optional<Pessoa> pessoa = repository.findById(id);
		if(pessoa.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontada");
		}
		repository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body("Pessoa deletada");
	}
	
}
