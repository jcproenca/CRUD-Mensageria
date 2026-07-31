package com.example.api1pessoa.runner;

import com.example.api1pessoa.model.Endereco;
import com.example.api1pessoa.model.Fisica;
import com.example.api1pessoa.model.Juridica;
import com.example.api1pessoa.model.Role;
import com.example.api1pessoa.model.Usuario;
import com.example.api1pessoa.repository.FisicaRepository;
import com.example.api1pessoa.repository.JuridicaRepository;
import com.example.api1pessoa.repository.UsuarioRepository;
import com.example.api1pessoa.service.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializerRunner implements CommandLineRunner {

    private final FisicaRepository fisicaRepository;
    private final JuridicaRepository juridicaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageProducer messageProducer;

    @Override
    public void run(String... args) {
        criarUsuarios();
        criarDadosMockados();
    }

    // Usuários padrão

    private void criarUsuarios() {
        if (usuarioRepository.findByUsername("user").isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .build());
            log.info("[DataInitializer] Usuário 'user' criado (role=USER, senha=user123)");
        }

        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build());
            log.info("[DataInitializer] Usuário 'admin' criado (role=ADMIN, senha=admin123)");
        }
    }

    // Dados mockados para testes manuais

    private void criarDadosMockados() {
        // Mock 1 – Pessoa Física
        if (fisicaRepository.findByCpf("12345678901").isEmpty()) {
            Endereco endFisica = Endereco.builder()
                    .rua("Avenida 24A")
                    .numero("1515")
                    .complemento("Apto 45")
                    .bairro("Bela Vista")
                    .cidade("Rio Claro")
                    .estado("SP")
                    .cep("13506-900")
                    .build();

            Fisica fisica = Fisica.builder()
                    .nome("João Frank")
                    .genero("Masculino")
                    .idade(30)
                    .cpf("12345678901")
                    .endereco(endFisica)
                    .build();

            Fisica salva = fisicaRepository.save(fisica);
            messageProducer.enviarMensagem(
                    "Pessoa Física '" + salva.getNome() + "' (id=" + salva.getId() + ") criada com sucesso");
            log.info("[DataInitializer] Pessoa Física mockada criada: {} (id={})", salva.getNome(), salva.getId());
        }

        // Mock 2 – Pessoa Jurídica
        if (juridicaRepository.findByCnpj("12345678000199").isEmpty()) {
            Endereco endJuridica = Endereco.builder()
                    .rua("Avenida 24A")
                    .numero("1515")
                    .complemento("UNESP RIO CLARO")
                    .bairro("Bela Vista")
                    .cidade("Rio Claro")
                    .estado("SP")
                    .cep("13506-900")
                    .build();

            Juridica juridica = Juridica.builder()
                    .nome("Empresa SPRING UNESP ")
                    .genero("Empresa")
                    .idade(5)
                    .cnpj("12345678000199")
                    .endereco(endJuridica)
                    .build();

            Juridica salva = juridicaRepository.save(juridica);
            messageProducer.enviarMensagem(
                    "Pessoa Jurídica '" + salva.getNome() + "' (id=" + salva.getId() + ") criada com sucesso");
            log.info("[DataInitializer] Pessoa Jurídica mockada criada: {} (id={})", salva.getNome(), salva.getId());
        }
    }
}
