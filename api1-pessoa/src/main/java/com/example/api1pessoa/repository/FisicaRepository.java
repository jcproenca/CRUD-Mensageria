package com.example.api1pessoa.repository;

import com.example.api1pessoa.model.Fisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FisicaRepository extends JpaRepository<Fisica, Long> {
    Optional<Fisica> findByCpf(String cpf);
}
