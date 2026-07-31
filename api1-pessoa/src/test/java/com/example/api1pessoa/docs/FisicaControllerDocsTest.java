package com.example.api1pessoa.docs;

import com.example.api1pessoa.dto.response.FisicaResponseDTO;
import com.example.api1pessoa.dto.EnderecoDTO;
import com.example.api1pessoa.service.FisicaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = com.example.api1pessoa.controller.FisicaController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@Import(com.example.api1pessoa.config.SecurityConfig.class)
public class FisicaControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FisicaService fisicaService;

    @MockBean
    private com.example.api1pessoa.repository.UsuarioRepository usuarioRepository;

    private FisicaResponseDTO responseDTO;
    private EnderecoDTO enderecoDTO;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Rua 11", "1", "Apto 45",
                "Vila Nova", "Rio Claro", "SP", "01001-000");
        responseDTO = new FisicaResponseDTO(1L, "João Frank", "Masculino", 30,
                "12345678901", enderecoDTO);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarTodos() throws Exception {
        Mockito.when(fisicaService.listarTodos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/fisicas").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("fisicas-listar",
                        responseFields(
                                fieldWithPath("[].id").description("Identificador único"),
                                fieldWithPath("[].nome").description("Nome completo"),
                                fieldWithPath("[].genero").description("Gênero"),
                                fieldWithPath("[].idade").description("Idade em anos"),
                                fieldWithPath("[].cpf").description("CPF (11 dígitos, sem formatação)"),
                                fieldWithPath("[].endereco.rua").description("Rua"),
                                fieldWithPath("[].endereco.numero").description("Número"),
                                fieldWithPath("[].endereco.complemento").description("Complemento (opcional)").optional(),
                                fieldWithPath("[].endereco.bairro").description("Bairro"),
                                fieldWithPath("[].endereco.cidade").description("Cidade"),
                                fieldWithPath("[].endereco.estado").description("UF (2 letras)"),
                                fieldWithPath("[].endereco.cep").description("CEP")
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void buscarPorId() throws Exception {
        Mockito.when(fisicaService.buscarPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/fisicas/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("fisicas-buscar-por-id",
                        pathParameters(parameterWithName("id").description("ID da Pessoa Física")),
                        responseFields(
                                fieldWithPath("id").description("Identificador único"),
                                fieldWithPath("nome").description("Nome completo"),
                                fieldWithPath("genero").description("Gênero"),
                                fieldWithPath("idade").description("Idade em anos"),
                                fieldWithPath("cpf").description("CPF (11 dígitos, sem formatação)"),
                                fieldWithPath("endereco.rua").description("Rua"),
                                fieldWithPath("endereco.numero").description("Número"),
                                fieldWithPath("endereco.complemento").description("Complemento (opcional)").optional(),
                                fieldWithPath("endereco.bairro").description("Bairro"),
                                fieldWithPath("endereco.cidade").description("Cidade"),
                                fieldWithPath("endereco.estado").description("UF (2 letras)"),
                                fieldWithPath("endereco.cep").description("CEP")
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void criar() throws Exception {
        Mockito.when(fisicaService.criar(any())).thenReturn(responseDTO);

        Map<String, Object> body = Map.of(
                "nome", "João Frank",
                "genero", "Masculino",
                "idade", 30,
                "cpf", "12345678901",
                "endereco", Map.of(
                        "rua", "Rua 11", "numero", "1",
                        "complemento", "Apto 45", "bairro", "Vila Nova",
                        "cidade", "Rio Claro", "estado", "SP", "cep", "00000-000"
                )
        );

        mockMvc.perform(post("/api/fisicas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentation.document("fisicas-criar",
                        requestFields(
                                fieldWithPath("nome").description("Nome completo"),
                                fieldWithPath("genero").description("Gênero"),
                                fieldWithPath("idade").description("Idade em anos"),
                                fieldWithPath("cpf").description("CPF (11 dígitos, sem formatação)"),
                                fieldWithPath("endereco.rua").description("Rua"),
                                fieldWithPath("endereco.numero").description("Número"),
                                fieldWithPath("endereco.complemento").description("Complemento (opcional)").optional(),
                                fieldWithPath("endereco.bairro").description("Bairro"),
                                fieldWithPath("endereco.cidade").description("Cidade"),
                                fieldWithPath("endereco.estado").description("UF (2 letras)"),
                                fieldWithPath("endereco.cep").description("CEP")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador único gerado"),
                                fieldWithPath("nome").description("Nome completo"),
                                fieldWithPath("genero").description("Gênero"),
                                fieldWithPath("idade").description("Idade em anos"),
                                fieldWithPath("cpf").description("CPF"),
                                fieldWithPath("endereco.rua").description("Rua"),
                                fieldWithPath("endereco.numero").description("Número"),
                                fieldWithPath("endereco.complemento").description("Complemento").optional(),
                                fieldWithPath("endereco.bairro").description("Bairro"),
                                fieldWithPath("endereco.cidade").description("Cidade"),
                                fieldWithPath("endereco.estado").description("UF"),
                                fieldWithPath("endereco.cep").description("CEP")
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizar() throws Exception {
        Mockito.when(fisicaService.atualizar(eq(1L), any())).thenReturn(responseDTO);

        Map<String, Object> body = Map.of(
                "nome", "João Frank",
                "genero", "Masculino",
                "idade", 30,
                "cpf", "12345678901",
                "endereco", Map.of(
                        "rua", "Rua 11", "numero", "1",
                        "complemento", "Apto 45", "bairro", "Vila Nova",
                        "cidade", "Rio Claro", "estado", "SP", "cep", "00000-000"
                )
        );

        mockMvc.perform(put("/api/fisicas/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("fisicas-atualizar",
                        pathParameters(parameterWithName("id").description("ID da Pessoa Física")),
                        requestFields(
                                fieldWithPath("nome").description("Nome completo"),
                                fieldWithPath("genero").description("Gênero"),
                                fieldWithPath("idade").description("Idade em anos"),
                                fieldWithPath("cpf").description("CPF"),
                                fieldWithPath("endereco.rua").description("Rua"),
                                fieldWithPath("endereco.numero").description("Número"),
                                fieldWithPath("endereco.complemento").description("Complemento").optional(),
                                fieldWithPath("endereco.bairro").description("Bairro"),
                                fieldWithPath("endereco.cidade").description("Cidade"),
                                fieldWithPath("endereco.estado").description("UF"),
                                fieldWithPath("endereco.cep").description("CEP")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador único"),
                                fieldWithPath("nome").description("Nome completo"),
                                fieldWithPath("genero").description("Gênero"),
                                fieldWithPath("idade").description("Idade em anos"),
                                fieldWithPath("cpf").description("CPF"),
                                fieldWithPath("endereco.rua").description("Rua"),
                                fieldWithPath("endereco.numero").description("Número"),
                                fieldWithPath("endereco.complemento").description("Complemento").optional(),
                                fieldWithPath("endereco.bairro").description("Bairro"),
                                fieldWithPath("endereco.cidade").description("Cidade"),
                                fieldWithPath("endereco.estado").description("UF"),
                                fieldWithPath("endereco.cep").description("CEP")
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletar() throws Exception {
        mockMvc.perform(delete("/api/fisicas/{id}", 1L).with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("fisicas-deletar",
                        pathParameters(parameterWithName("id").description("ID da Pessoa Física a remover"))
                ));
    }
}
