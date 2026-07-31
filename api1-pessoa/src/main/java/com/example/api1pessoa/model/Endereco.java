package com.example.api1pessoa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @NotBlank
    @Column(name = "endereco_rua", nullable = false)
    private String rua;

    @NotBlank
    @Column(name = "endereco_numero", nullable = false)
    private String numero;

    @Column(name = "endereco_complemento")
    private String complemento;

    @NotBlank
    @Column(name = "endereco_bairro", nullable = false)
    private String bairro;

    @NotBlank
    @Column(name = "endereco_cidade", nullable = false)
    private String cidade;

    @NotBlank
    @Column(name = "endereco_estado", nullable = false, length = 2)
    private String estado;

    @NotBlank
    @Column(name = "endereco_cep", nullable = false, length = 9)
    private String cep;
}
