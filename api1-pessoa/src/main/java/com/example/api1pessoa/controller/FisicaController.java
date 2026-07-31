package com.example.api1pessoa.controller;

import com.example.api1pessoa.dto.FisicaRequestDTO;
import com.example.api1pessoa.dto.response.FisicaResponseDTO;
import com.example.api1pessoa.service.FisicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/fisicas")
@RequiredArgsConstructor
public class FisicaController {

    private final FisicaService fisicaService;


    @GetMapping
    public ResponseEntity<List<FisicaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(fisicaService.listarTodos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<FisicaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fisicaService.buscarPorId(id));
    }


    @PostMapping
    public ResponseEntity<FisicaResponseDTO> criar(@Valid @RequestBody FisicaRequestDTO dto) {
        FisicaResponseDTO criada = fisicaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FisicaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody FisicaRequestDTO dto) {
        return ResponseEntity.ok(fisicaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fisicaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
