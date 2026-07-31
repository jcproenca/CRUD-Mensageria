package com.example.api1pessoa.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record JuridicaRequestDTO(
        @NotBlank String nome,
        @NotBlank String genero,
        @NotNull @Positive Integer idade,
        @NotBlank @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos numéricos") String cnpj,
        @Valid @NotNull EnderecoDTO endereco
) {
}
