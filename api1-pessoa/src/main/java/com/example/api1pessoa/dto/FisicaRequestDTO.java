package com.example.api1pessoa.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record FisicaRequestDTO(
        @NotBlank String nome,
        @NotBlank String genero,
        @NotNull @Positive Integer idade,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos") String cpf,
        @Valid @NotNull EnderecoDTO endereco
) {
}
