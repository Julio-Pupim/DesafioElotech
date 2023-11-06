package org.elotech.desafio.dto;

import org.elotech.desafio.model.Pessoa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContatoRecordDTO(@NotNull @NotBlank String nome, @NotNull @NotBlank String telefone, @NotNull @NotBlank String email, Pessoa pessoa) {

}

