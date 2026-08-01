package com.example.api1pessoa.docs;

import com.example.api1pessoa.dto.EnderecoDTO;
import com.example.api1pessoa.dto.response.JuridicaResponseDTO;
import com.example.api1pessoa.service.JuridicaService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@ActiveProfiles("test")
@WebMvcTest(controllers = com.example.api1pessoa.controller.JuridicaController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@Import(com.example.api1pessoa.config.SecurityConfig.class)
public class JuridicaControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JuridicaService juridicaService;

    @MockBean
    private com.example.api1pessoa.repository.UsuarioRepository usuarioRepository;

    private JuridicaResponseDTO responseDTO;
    private EnderecoDTO enderecoDTO;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Avenida 24A", "1515", "Sala 1",
                "Bela Vista", "Rio Claro", "SP", "00000-000");
        responseDTO = new JuridicaResponseDTO(1L, "UNESP Ltda", "Empresa", 5,
                "12345678000199", enderecoDTO);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarTodos() throws Exception {
        Mockito.when(juridicaService.listarTodos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/juridicas").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("juridicas-listar",
                        responseFields(
                                fieldWithPath("[].id").description("Identificador único"),
                                fieldWithPath("[].nome").description("Razão social / nome da empresa"),
                                fieldWithPath("[].genero").description("Gênero ou tipo de pessoa jurídica"),
                                fieldWithPath("[].idade").description("Idade em anos (tempo de existência)"),
                                fieldWithPath("[].cnpj").description("CNPJ (14 dígitos, sem formatação)"),
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
        Mockito.when(juridicaService.buscarPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/juridicas/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("juridicas-buscar-por-id",
                        pathParameters(parameterWithName("id").description("ID da Pessoa Jurídica")),
                        responseFields(
                                fieldWithPath("id").description("Identificador único"),
                                fieldWithPath("nome").description("Razão social / nome"),
                                fieldWithPath("genero").description("Gênero / tipo"),
                                fieldWithPath("idade").description("Tempo de existência em anos"),
                                fieldWithPath("cnpj").description("CNPJ (14 dígitos)"),
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
    void criar() throws Exception {
        Mockito.when(juridicaService.criar(any())).thenReturn(responseDTO);

        Map<String, Object> body = Map.of(
                "nome", "UNESP Ltda",
                "genero", "Empresa",
                "idade", 5,
                "cnpj", "12345678000199",
                "endereco", Map.of(
                        "rua", "Avenida 24A", "numero", "1515",
                        "complemento", "Sala 1", "bairro", "Bela Vista",
                        "cidade", "Rio Claro", "estado", "SP", "cep", "00000-000"
                )
        );

        mockMvc.perform(post("/api/juridicas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentation.document("juridicas-criar",
                        requestFields(
                                fieldWithPath("nome").description("Razão social / nome"),
                                fieldWithPath("genero").description("Gênero / tipo"),
                                fieldWithPath("idade").description("Tempo de existência em anos"),
                                fieldWithPath("cnpj").description("CNPJ (14 dígitos, sem formatação)"),
                                fieldWithPath("endereco.rua").description("Rua"),
                                fieldWithPath("endereco.numero").description("Número"),
                                fieldWithPath("endereco.complemento").description("Complemento").optional(),
                                fieldWithPath("endereco.bairro").description("Bairro"),
                                fieldWithPath("endereco.cidade").description("Cidade"),
                                fieldWithPath("endereco.estado").description("UF (2 letras)"),
                                fieldWithPath("endereco.cep").description("CEP")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador único gerado"),
                                fieldWithPath("nome").description("Razão social / nome"),
                                fieldWithPath("genero").description("Gênero / tipo"),
                                fieldWithPath("idade").description("Tempo de existência em anos"),
                                fieldWithPath("cnpj").description("CNPJ"),
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
        Mockito.when(juridicaService.atualizar(eq(1L), any())).thenReturn(responseDTO);

        Map<String, Object> body = Map.of(
                "nome", "UNESP Ltda",
                "genero", "Empresa",
                "idade", 5,
                "cnpj", "12345678000199",
                "endereco", Map.of(
                        "rua", "Avenida 24A", "numero", "1515",
                        "complemento", "Sala 1", "bairro", "Bela Vista",
                        "cidade", "Rio Claro", "estado", "SP", "cep", "00000-000"
                )
        );

        mockMvc.perform(put("/api/juridicas/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("juridicas-atualizar",
                        pathParameters(parameterWithName("id").description("ID da Pessoa Jurídica")),
                        requestFields(
                                fieldWithPath("nome").description("Razão social / nome"),
                                fieldWithPath("genero").description("Gênero / tipo"),
                                fieldWithPath("idade").description("Tempo de existência em anos"),
                                fieldWithPath("cnpj").description("CNPJ"),
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
                                fieldWithPath("nome").description("Razão social / nome"),
                                fieldWithPath("genero").description("Gênero / tipo"),
                                fieldWithPath("idade").description("Tempo de existência em anos"),
                                fieldWithPath("cnpj").description("CNPJ"),
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
        mockMvc.perform(delete("/api/juridicas/{id}", 1L).with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("juridicas-deletar",
                        pathParameters(parameterWithName("id").description("ID da Pessoa Jurídica a remover"))
                ));
    }
}
