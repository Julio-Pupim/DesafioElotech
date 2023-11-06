package org.elotech.desafio.dto;

import java.time.LocalDate;
import java.util.List;

import org.elotech.desafio.model.Contato;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record PessoaRecordDTO(@NotNull @NotBlank String nome, @NotNull @NotBlank String cpf,@NotNull @Past LocalDate dataNascimento, @NotEmpty List<Contato> contatosList) {

}
