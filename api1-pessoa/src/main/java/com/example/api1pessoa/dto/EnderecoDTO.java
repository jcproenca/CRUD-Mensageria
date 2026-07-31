package com.example.api1pessoa.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoDTO(
        @NotBlank String rua,
        @NotBlank String numero,
        String complemento,
        @NotBlank String bairro,
        @NotBlank String cidade,
        @NotBlank String estado,
        @NotBlank String cep
) {
}
