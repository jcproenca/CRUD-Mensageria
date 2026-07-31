package com.example.api1pessoa.repository;

import com.example.api1pessoa.model.Juridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuridicaRepository extends JpaRepository<Juridica, Long> {
    Optional<Juridica> findByCnpj(String cnpj);
}
