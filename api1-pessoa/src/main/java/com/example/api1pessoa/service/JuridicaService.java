package com.example.api1pessoa.service;

import com.example.api1pessoa.dto.EnderecoDTO;
import com.example.api1pessoa.dto.JuridicaRequestDTO;
import com.example.api1pessoa.dto.response.JuridicaResponseDTO;
import com.example.api1pessoa.exception.ResourceNotFoundException;
import com.example.api1pessoa.model.Endereco;
import com.example.api1pessoa.model.Juridica;
import com.example.api1pessoa.repository.JuridicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JuridicaService {

    private final JuridicaRepository juridicaRepository;
    private final MessageProducer messageProducer;

    public List<JuridicaResponseDTO> listarTodos() {
        return juridicaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public JuridicaResponseDTO buscarPorId(Long id) {
        Juridica juridica = buscarEntidadePorId(id);
        return toResponseDTO(juridica);
    }

    public JuridicaResponseDTO criar(JuridicaRequestDTO dto) {
        Juridica juridica = Juridica.builder()
                .nome(dto.nome())
                .genero(dto.genero())
                .idade(dto.idade())
                .cnpj(dto.cnpj())
                .endereco(toEndereco(dto.endereco()))
                .build();

        Juridica salva = juridicaRepository.save(juridica);
        messageProducer.enviarMensagem("Pessoa Jurídica '" + salva.getNome() + "' (id=" + salva.getId() + ") criada com sucesso");
        return toResponseDTO(salva);
    }

    public JuridicaResponseDTO atualizar(Long id, JuridicaRequestDTO dto) {
        Juridica juridica = buscarEntidadePorId(id);
        juridica.setNome(dto.nome());
        juridica.setGenero(dto.genero());
        juridica.setIdade(dto.idade());
        juridica.setCnpj(dto.cnpj());
        juridica.setEndereco(toEndereco(dto.endereco()));

        Juridica atualizada = juridicaRepository.save(juridica);
        messageProducer.enviarMensagem("Pessoa Jurídica '" + atualizada.getNome() + "' (id=" + atualizada.getId() + ") atualizada com sucesso");
        return toResponseDTO(atualizada);
    }

    public void deletar(Long id) {
        Juridica juridica = buscarEntidadePorId(id);
        juridicaRepository.delete(juridica);
        messageProducer.enviarMensagem("Pessoa Jurídica '" + juridica.getNome() + "' (id=" + id + ") deletada com sucesso");
    }

    private Juridica buscarEntidadePorId(Long id) {
        return juridicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa Jurídica não encontrada com id: " + id));
    }

    private Endereco toEndereco(EnderecoDTO dto) {
        return Endereco.builder()
                .rua(dto.rua())
                .numero(dto.numero())
                .complemento(dto.complemento())
                .bairro(dto.bairro())
                .cidade(dto.cidade())
                .estado(dto.estado())
                .cep(dto.cep())
                .build();
    }

    private EnderecoDTO toEnderecoDTO(Endereco endereco) {
        return new EnderecoDTO(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }

    private JuridicaResponseDTO toResponseDTO(Juridica juridica) {
        return new JuridicaResponseDTO(
                juridica.getId(),
                juridica.getNome(),
                juridica.getGenero(),
                juridica.getIdade(),
                juridica.getCnpj(),
                toEnderecoDTO(juridica.getEndereco())
        );
    }
}
