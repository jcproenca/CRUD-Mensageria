package com.example.api1pessoa.dto.response;

import com.example.api1pessoa.dto.EnderecoDTO;

public record FisicaResponseDTO(
        Long id,
        String nome,
        String genero,
        Integer idade,
        String cpf,
        EnderecoDTO endereco
) {
}
