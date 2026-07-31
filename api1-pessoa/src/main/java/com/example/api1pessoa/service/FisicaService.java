package com.example.api1pessoa.service;

import com.example.api1pessoa.dto.EnderecoDTO;
import com.example.api1pessoa.dto.FisicaRequestDTO;
import com.example.api1pessoa.dto.response.FisicaResponseDTO;
import com.example.api1pessoa.exception.ResourceNotFoundException;
import com.example.api1pessoa.model.Endereco;
import com.example.api1pessoa.model.Fisica;
import com.example.api1pessoa.repository.FisicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FisicaService {

    private final FisicaRepository fisicaRepository;
    private final MessageProducer messageProducer;

    public List<FisicaResponseDTO> listarTodos() {
        return fisicaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public FisicaResponseDTO buscarPorId(Long id) {
        Fisica fisica = buscarEntidadePorId(id);
        return toResponseDTO(fisica);
    }

    public FisicaResponseDTO criar(FisicaRequestDTO dto) {
        Fisica fisica = Fisica.builder()
                .nome(dto.nome())
                .genero(dto.genero())
                .idade(dto.idade())
                .cpf(dto.cpf())
                .endereco(toEndereco(dto.endereco()))
                .build();

        Fisica salva = fisicaRepository.save(fisica);
        messageProducer.enviarMensagem("Pessoa Física '" + salva.getNome() + "' (id=" + salva.getId() + ") criada com sucesso");
        return toResponseDTO(salva);
    }

    public FisicaResponseDTO atualizar(Long id, FisicaRequestDTO dto) {
        Fisica fisica = buscarEntidadePorId(id);
        fisica.setNome(dto.nome());
        fisica.setGenero(dto.genero());
        fisica.setIdade(dto.idade());
        fisica.setCpf(dto.cpf());
        fisica.setEndereco(toEndereco(dto.endereco()));

        Fisica atualizada = fisicaRepository.save(fisica);
        messageProducer.enviarMensagem("Pessoa Física '" + atualizada.getNome() + "' (id=" + atualizada.getId() + ") atualizada com sucesso");
        return toResponseDTO(atualizada);
    }

    public void deletar(Long id) {
        Fisica fisica = buscarEntidadePorId(id);
        fisicaRepository.delete(fisica);
        messageProducer.enviarMensagem("Pessoa Física '" + fisica.getNome() + "' (id=" + id + ") deletada com sucesso");
    }

    private Fisica buscarEntidadePorId(Long id) {
        return fisicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa Física não encontrada com id: " + id));
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

    private FisicaResponseDTO toResponseDTO(Fisica fisica) {
        return new FisicaResponseDTO(
                fisica.getId(),
                fisica.getNome(),
                fisica.getGenero(),
                fisica.getIdade(),
                fisica.getCpf(),
                toEnderecoDTO(fisica.getEndereco())
        );
    }
}
