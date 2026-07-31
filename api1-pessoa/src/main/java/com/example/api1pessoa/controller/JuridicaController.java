package com.example.api1pessoa.controller;

import com.example.api1pessoa.dto.JuridicaRequestDTO;
import com.example.api1pessoa.dto.response.JuridicaResponseDTO;
import com.example.api1pessoa.service.JuridicaService;
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
@RequestMapping("/api/juridicas")
@RequiredArgsConstructor
public class JuridicaController {

    private final JuridicaService juridicaService;

    @GetMapping
    public ResponseEntity<List<JuridicaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(juridicaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuridicaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(juridicaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<JuridicaResponseDTO> criar(@Valid @RequestBody JuridicaRequestDTO dto) {
        JuridicaResponseDTO criada = juridicaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuridicaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody JuridicaRequestDTO dto) {
        return ResponseEntity.ok(juridicaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        juridicaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
